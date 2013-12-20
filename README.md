<strong>Steps to register Service Broker (Gemfire version) with CloudFoundry</strong>
<ol>
<li>Edit the values <code>src/main/resources/application.properties</code> to reflect your gemfire install directory</li>
<li>Start the <code>JavaServiceBrokerApplication</code> Spring Boot Application with the following java property:
<pre>spring.profiles.active=gemfire</pre>
</li>
<li>Register service broker with CloudFoundry using cf add-service-broker.  Make sure your URL/IP for your broker is resolvable by your CloudFoundry env.<br>
Sample output: <br>
<pre>$ cf add-service-broker gemfire

URL> http://10.0.0.13:8080<br>
Username> admin
Password> admin

Adding service broker gemfire... OK

$ cf service-brokers

Getting service brokers... OK

Name      URL                  
gemfire   http://10.0.0.13:8080</pre>
</li>
<li>To flip flag on a service plan to be public:<br>
note: this is required.  Without it you will not be able to create an instance of this service.  Also, make sure you've logged in using <code>cf login</code> recently as your auth token is only cached for 10 minutes
<pre>cf curl PUT /v2/service_plans/$GUID -b '{"public":true}'</pre>
** GUID = the GUID in the metadata section returned from /v2/service_plans API invocation
<br><br>
Helpful CF Web service calls:<br>
To get list of service plans:
<pre>cf curl GET /v2/service_plans

Example:
$ cf curl GET /v2/service_plans

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
<pre>cf curl GET /v2/service_plans/$GUID/service_instances</pre>
** GUID = the GUID in the metadata section returned from /v2/service_plans
</li>
<li>Create a service instance of Gemfire using <code>cf create-service</code>
<pre>
$ cf create-service

1: p-gemfire , via 
2: user-provided , via 
What kind?> 1

Name?> p-gemfire-f076a

1: 1GB-replicated: Multi-tenant Gemfire service; 1GB data storage replicated
Which plan?> 1

Creating service p-gemfire-f076a... OK
</pre>
At this point you will have a single Gemfire JVM associated with the unique service instance ID within your <code>$gemfire.data</code> directory:
<pre>
$ ls -l
total 120
-rw-r--r--@  1 azwickey  staff  31830 Mar  4  2013 EULA.txt
drwxr-xr-x@  9 azwickey  staff    306 Mar  4  2013 SampleCode
drwxr-xr-x   5 azwickey  staff    170 Dec  6 16:53 a082873a-6691-4ec9-bc17-f0d45fce1fba      *** This is the Gemfire server **
-rw-r--r--   1 azwickey  staff  15127 Dec  6 16:53 a082873a-6691-4ec9-bc17-f0d45fce1fba.log  *** This is the log file for the Gemfire server **
drwxr-xr-x@ 10 azwickey  staff    340 Mar  4  2013 bin
-rw-r--r--   1 azwickey  staff    200 Dec  6 16:53 cf_service.out
drwxr-xr-x@  4 azwickey  staff    136 Mar  4  2013 defaultConfigs
drwxr-xr-x@  9 azwickey  staff    306 Mar  4  2013 docs
drwxr-xr-x@ 17 azwickey  staff    578 Mar  4  2013 dtd
-rw-r--r--   1 azwickey  staff   7108 Dec  6 16:53 gfsh-2013-12-06_16-53-14.log
drwxr-xr-x@ 34 azwickey  staff   1156 Mar  4  2013 lib
drwxr-xr-x@  3 azwickey  staff    102 Mar  4  2013 templates
drwxr-xr-x@  7 azwickey  staff    238 Mar  4  2013 tools
</pre>
</li>
<li>next steps to be added once broker is more functional...</li>
</ol>


start locator --name=locator --force=true --mcast-port=0 --log-level=fine --properties-file=locator.properties
start server --name=server1 --locators=10.0.0.13[10334] --cache-xml-file=server.xml --properties-file=gemfire.properties
