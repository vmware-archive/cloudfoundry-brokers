<strong>Steps to register Service Broker (Gemfire version) with CloudFoundry</strong>
<ol>
<li>Start the <pre>JavaServiceBrokerApplication<pre> Spring Boot Application with the following java property:
<code>spring.profiles.active=gemfire</code>
</li>
<li> </li>
<li> </li>
<li> </li>
<li> </li>
</ol>

To get list of service plans:
cf curl GET /v2/service_plans

To flip flag on a service plan to be public:
cf curl PUT /v2/service_plans/$GUID -b '{"public":true}'
** GUID = the GUID in the metadata section returned from /v2/service_plans

To get the instances provisioned by this service:
cf curl GET /v2/service_plans/$GUID/service_instances
** GUID = the GUID in the metadata section returned from /v2/service_plans