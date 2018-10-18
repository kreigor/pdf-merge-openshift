# PDF Merge App for Openshift

###### I built the PDF Merge utility years ago as my first spring web app.
###### I decided to see if I could get it to work in openshift and it did with little changes needed.

* Maven
* Spring Boot 2.0.5
* Thymeleaf
* IText PDF v5
* Wildfly
* Openshift

#### Can be deployed to OpenShift using WildFly
###### The following deployment env variable needs to be set
* SPRING_PROFILES_ACTIVE = openshift