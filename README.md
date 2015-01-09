JerseyFX
========

#SSL and Jersey

##Generate client and server keys

keytool -genkey -keystore keystore_client -alias clientKey -dname “CN=www.aruld.info, OU=R&D, O=Vasun Technologies, L=Billings, ST=Montana, C=US”
keytool -genkey -keystore keystore_server -alias serverKey -dname “CN=www.aruld.info, OU=R&D, O=Vasun Technologies, L=Billings, ST=Montana, C=US”


##Generate client and server certificates

keytool -export -alias clientKey -rfc -keystore keystore_client > client.cert
keytool -export -alias serverKey -rfc -keystore keystore_server > server.cert


##Import certificates to corresponding truststores

keytool -import -alias clientCert -file client.cert -keystore truststore_server
keytool -import -alias serverCert -file server.cert -keystore truststore_client
