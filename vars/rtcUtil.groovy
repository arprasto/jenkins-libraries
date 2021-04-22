/*
 * Script to print timelines and iterations from a given project area.
 * To run:
 *   - set/export env vars for RTC_REPO_URL, RTC_USER_ID and RTC_PASSWORD
 *   - run: groovy -cp "<buildsystem/buildtoolkit dir>/*" PrintTimelines.groovy SRC_PROJECT_NAME
 */

import com.ibm.team.repository.common.*
import com.ibm.team.repository.common.query.*
import com.ibm.team.repository.client.*
import com.ibm.team.repository.client.login.*
import com.ibm.team.process.common.*
import com.ibm.team.process.client.*

def REPO_URL = System.getenv("RTC_REPO_URL") ?: "https://localhost:9443/jazz"
def USER_ID = System.getenv("RTC_USER_ID") ?: "ADMIN"
def PASSWORD = System.getenv("RTC_PASSWORD") ?: "ADMIN"

def p(obj) {
	println "${obj}"
}

class RTC {
	def repo

	class LoginHandler implements ILoginHandler2 {
		def userId, password

		def ILoginInfo2 challenge(ITeamRepository repo) {
	//        println "${userId} - ${password}"
			new UsernameAndPasswordLoginInfo(userId, password)
		}
	}

	RTC(repoUrl, userId, password) {
		repo = TeamPlatform.teamRepositoryService.getUnmanagedRepository(repoUrl)
		repo.registerLoginHandler(new LoginHandler(userId: userId, password: password))
	}

	def login(monitor = null) {
		repo.login(monitor)
	}

	def IProcessItemService processClient() {
		repo.getClientLibrary(IProcessItemService.class)
	}

	def fetch(IItemHandle itemHandle, List<String> properties = null, flags = IItemManager.DEFAULT, monitor = null) {
		if (properties == null)
			repo.itemManager.fetchCompleteItem(itemHandle, flags, monitor)
		else
			repo.itemManager.fetchPartialItem(itemHandle, flags, properties, monitor)
	}

	def fetchMulti(List<IItemHandle> itemHandles, List<String> properties = null, flags = IItemManager.DEFAULT, monitor = null) {
		if (properties == null)
			repo.itemManager.fetchCompleteItemsPermissionAware(itemHandles, flags, monitor).retrievedItems
		else
			repo.itemManager.fetchPartialItemsPermissionAware(itemHandles, flags, properties, monitor).retrievedItems
	}

//	def fetchAll(ItemQueryIterator iter, properties = null, flags = IItemManager.DEFAULT, monitor = null) {
//		def items = []
//		while (iter.hasNext(monitor)) {
//			def handles = iter.next(100, monitor)
//			items.addAll(fetchMulti(handles, properties, flags, monitor))
//		}
//		items
//	}

	def projectAreas(properties = null, monitor = null) {
		processClient().findAllProjectAreas(properties, monitor)
	}

	def IProjectArea findProjectArea(name, properties = null, monitor = null) {
		processClient().findProcessArea(new URI(null, null, name, null), properties, monitor)
	}

}

def printIterations(rtc, iterationHandles, depth) {
	def List<IIteration> iterations = rtc.fetchMulti(Arrays.asList(iterationHandles))
	for (iteration in iterations) {
		p "  ${'  ' * depth}id: '${iteration.id}', label: '${iteration.label}', startDate: ${iteration.startDate}, endDate: ${iteration.endDate}, hasDeliverable: ${iteration.hasDeliverable()}, archived: ${iteration.archived}"
		printIterations(rtc, iteration.children, depth+1)
	}
}

def test(SRC_PROJECT_NAME)
{

p "Using env vars RTC_REPO_URL: ${REPO_URL}, RTC_USER_ID: ${USER_ID}, RTC_PASSWORD: ${'*' * PASSWORD.length()}"

p "args: ${SRC_PROJECT_NAME}"
if (args.size() != 1) {
	p "usage: CopyTimelines SRC_PROJECT_NAME"
	System.exit(1)
}

def srcProjectName = args[0]

p "Logging in ..."
TeamPlatform.startup()
def rtc = new RTC(REPO_URL, USER_ID, PASSWORD)
rtc.login()

//def projectAreas = rtc.projectAreas(['name', 'archived']).sort({it.name})
//for (projectArea in projectAreas) {
//	p "project area '${projectArea.name}'${projectArea.archived ? ' (archived)' : ''}:"
//}
//p "---"
//p "Total projects: ${projectAreas.size()}"

p "Fetching project '${srcProjectName}'..."
def srcProject = rtc.findProjectArea(srcProjectName)
assert srcProject != null, "could not find source project named '${srcProjectName}'"

//def destProject = rtc.findProjectArea(destProjectName)
//assert destProject != null, "could not find dest project named '${destProjectName}'"

def List<IDevelopmentLine> srcDevLines = rtc.fetchMulti(Arrays.asList(srcProject.developmentLines))
p "Source project has ${srcDevLines.size()} development lines:"
for (line in srcDevLines) {
	p "  line id: '${line.id}', label: '${line.label}', startDate: ${line.startDate}, endDate: ${line.endDate}, archived: ${line.archived}"
	p "  iterations (${line.iterations.size()} top-level):"
	printIterations(rtc, line.iterations, 1)
}
}
