def call(CHECKOUT_PATH){
echo "creating checkout dir ${CHECKOUT_PATH}"
sh """ chmod 777 ${CHECKOUT_PATH}/..
sh """ mkdir ${CHECKOUT_PATH} """
echo "setting permissions for ${CHECKOUT_PATH}"
sh """ chmod 777 ${CHECKOUT_PATH} """
}
