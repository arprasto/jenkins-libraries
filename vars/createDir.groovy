import java.io.File

def call(CHECKOUT_PATH){
def File f = new File(CHECKOUT_PATH);
if ("${f.isDirectory()}".matches("true")){
echo "folder already exists ${CHECKOUT_PATH} skipping create dir"
}
else{
echo "creating checkout dir ${CHECKOUT_PATH}"
sh """ mkdir ${CHECKOUT_PATH} """
echo "setting permissions for ${CHECKOUT_PATH}"
sh """ chmod 777 ${CHECKOUT_PATH} """
}
}
