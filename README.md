<strong>Steps to start and register Service Broker (Gemfire version) with CloudFoundry.  Note, steps 1 and 2 are only required if you are not using a bosh deployment of Gemfire and the Service Broker.  See [https://github.com/Pivotal-Field-Engineering/gemfire-bosh-release](https://github.com/Pivotal-Field-Engineering/gemfire-bosh-release). </strong>
<ol>
<li>The <code>JavaServiceBrokerApplication</code> requires a running gemfire cluster and at least one locator.  This project contains a Spring Data for Gemfire configuration and application harness.  This server configuration is required because it contains the required gemfire functions and regions for the service broker to operate.
<ul>
Start a gemfire locator.  For example, from GFSH: <pre>start locator --name=locator --force=true --mcast-port=0 --log-level=fine</pre>
</ul>
<ul>
Start atleast one gemfire server by launching <code>com.pivotal.cloudfoundry.service.broker.gemfire.GemfireServer</code>.  You will need the following java properties:
<pre>locators=$LOCATORHOST_PORT:  localhost[10334] is the default.  This is only required if you have a different host/port combo
gemfire-name=$NAME:  the name of the gemfire server.  gemfire-server is the default
spring.profiles.active=gemfire-server:  required to activate the appropriate spring profile and gemfire spring beams<br>
</pre>
</ul>
</li>
<li>Start the <code>JavaServiceBrokerApplication</code> Spring Boot Application with the following java property:
<pre>spring.profiles.active=gemfire-service-broker</pre>
</li>
<li>Register service broker with CloudFoundry using cf add-service-broker.  Make sure your URL/IP for your broker is resolvable by your CloudFoundry env.<br>
example: <br>
<pre>cf create-service-broker gemfire admin admin http://10.244.0.102:8888</pre>
</li>
<li>To flip flag on a service plan to be public:<br>
note: this is required.  Without it you will not be able to create an instance of this service.  Also, make sure you've logged in using <code>cf login</code> recently as your auth token is only cached for 10 minutes
<pre>
GUID=$(cf curl /v2/service_plans -X 'GET' | grep '"guid":' | sed 's/\"guid": //' | tr -d ' ",')
cf curl /v2/service_plans/$GUID -X 'PUT' -d '{"public":true}'
</pre>
** GUID = the GUID in the metadata section returned from /v2/service_plans API invocation
<br><br>
Helpful CF Web service calls:<br>
To get list of service plans:
<pre>cf curl GET /v2/service_plans

Example:
$ cf curl /v2/service_plans -X 'GET'

{
  "total_results": 1,
  "total_pages": 1,
  "prev_url": null,
  "next_url": null,
  "resources": [
    {
      "metadata": {
        "guid": "0a1d21f7-6c91-49bd-ab8b-43198ac0f65b",    **** This is the value to use ****
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
<pre>
GUID=$(cf curl /v2/service_plans -X 'GET' | grep '"guid":' | sed 's/\"guid": //' | tr -d ' ",')
cf curl /v2/service_plans/$GUID -X 'GET'
</pre>
** GUID = the GUID in the metadata section returned from /v2/service_plans
</li>
<li>Create a service instance of Gemfire using <code>cf create-service</code>
<pre>
cf create-service pivotal-gemfire 1GB-replicated pivotal-gemfire-1
</pre>
</li>
</ol>
