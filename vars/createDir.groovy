import java.nio.file.Files

def call(CHECKOUT_PATH){
if (Files.exists("${CHECKOUT_PATH}")){
echo "folder already exists ${CHECKOUT_PATH}"
}
else{
echo "creating checkout dir ${CHECKOUT_PATH}"
sh """ mkdir ${CHECKOUT_PATH} """
echo "setting permissions for ${CHECKOUT_PATH}"
sh """ chmod 777 ${CHECKOUT_PATH} """
}
}
