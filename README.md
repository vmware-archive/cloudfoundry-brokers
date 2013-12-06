<strong>Steps to register Service Broker (Gemfire version) with CloudFoundry</strong>
<ol>
<li>Edit the values <code>src/main/resources/application.properties</code> to reflect your gemfire install directory</li>
<li>Start the <code>JavaServiceBrokerApplication</code> Spring Boot Application with the following java property:
<pre>spring.profiles.active=gemfire</pre>
</li>
<li>Register service broker with CloudFoundry using cf add-service-broker.  Make sure your URL/IP for your broker is resolvable by your CloudFoundry env.<br>
Sample output: 
<pre>$ cf add-service-broker gemfire
<br><br>
URL> http://10.0.0.13:8080<br>
Username> admin
Password> admin
<br>
Adding service broker gemfire... OK
<br><br>
$ cf service-brokers
<br>
Getting service brokers... OK
<br>
Name      URL                  
gemfire   http://10.0.0.13:8080</pre>
</li>
<li>To flip flag on a service plan to be public:<br>
note: this is required.  Without it you will not be able to create an instance of this service
<pre>cf curl PUT /v2/service_plans/$GUID -b '{"public":true}'</pre>
** GUID = the GUID in the metadata section returned from /v2/service_plans API invocation
<br><br>
Helpful CF Web service calls:<br>
To get list of service plans:
<pre>cf curl GET /v2/service_plans
<br><br>
Example:<br><br>
$ cf curl GET /v2/service_plans
Failed to load admin-cf-plugin:
  Unable to activate admin-cf-plugin-3.0.0, because cfoundry-4.6.1 conflicts with cfoundry (< 3.0, >= 2.1.0)

You may need to update or remove this plugin.

{
  "total_results": 1,
  "total_pages": 1,
  "prev_url": null,
  "next_url": null,
  "resources": [
    {
      "metadata": {
        "guid": "0a1d21f7-6c91-49bd-ab8b-43198ac0f65b",
        "url": "/v2/service_plans/0a1d21f7-6c91-49bd-ab8b-43198ac0f65b",
        "created_at": "2013-12-06T21:38:38+00:00",
        "updated_at": null
      },
      "entity": {
        "name": "1GB-replicated",
        "free": true,
        "description": "Multi-tenant Gemfire service; 1GB data storage replicated",
        "service_guid": "0de626d0-fb81-4f42-a3f6-d72d75169918",
        "extra": "{\"cost\":\"free\"}",
        "unique_id": "4618z98-ab16-3t22-ba6e-1f258d3addz2",
        "public": false,
        "service_url": "/v2/services/0de626d0-fb81-4f42-a3f6-d72d75169918",
        "service_instances_url": "/v2/service_plans/0a1d21f7-6c91-49bd-ab8b-43198ac0f65b/service_instances"
      }
    }
  ]
}
</pre>

To get the instances provisioned by this service:
<pre>cf curl GET /v2/service_plans/$GUID/service_instances</pre>
** GUID = the GUID in the metadata section returned from /v2/service_plans
</li>
<li>create service</li>
<li>next step...</li>
</ol>

