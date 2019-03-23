JaxWsServer-256
===============

Пример создания SOAP - веб-сервиса с подписью 
по спецификации 2.5.6 и проверкой входящих сообщений.

Простейший конфигуратор сделан на JSF2+Primefaces3.4

Для того чтобы сгенерить(wsimport) классы по wsdl в которой данные передаются как body так и в header используйте параметр -XAdditionalHeaders.

Например:
wsimport -keep -XNocompile -XAdditionalHeaders -verbose http://xxx.xxx/service?wsdl
