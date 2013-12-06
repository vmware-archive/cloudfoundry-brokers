<strong>Steps to register Service Broker (Gemfire version) with CloudFoundry</strong>
<ol>
<li>Edit the values <code>src/main/resources/application.properties</code> to reflect your gemfire install directory</li>
<li>Start the <code>JavaServiceBrokerApplication</code> Spring Boot Application with the following java property:
<pre>spring.profiles.active=gemfire</pre>
</li>
<li>next step</li>
<li>To flip flag on a service plan to be public:
<code>cf curl PUT /v2/service_plans/$GUID -b '{"public":true}'</code>
** GUID = the GUID in the metadata section returned from /v2/service_plans

Helpful CF Web service calls:
To get list of service plans:
cf curl GET /v2/service_plans

To get the instances provisioned by this service:
cf curl GET /v2/service_plans/$GUID/service_instances
** GUID = the GUID in the metadata section returned from /v2/service_plans
</li>
<li>create service</li>
<li>next step...</li>
</ol>

