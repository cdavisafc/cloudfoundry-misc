Echo Service Client Application
===============================

Contained here is an updated version of the client application originally posted as a part of an [article](http://support.cloudfoundry.com/entries/20485171-how-to-add-a-system-service-to-oss-cloud-foundry-step-by-step-guide) on adding services to cloud foundry. I did not write the bulk of the code included here, just made some mods to the json parser utility and a slight change to the index.jsp for the app.

If you have an echo service installed into cloud foundry, either as a native cloud foundry service or as a brokered service, you can push this app, bind to that service and then access the web app. Don't forget to start your Echo Server (which can also be found within this github repository).
