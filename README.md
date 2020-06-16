# iam-login
**IAM register and login service RESTful endpoints (end to end setup, from DNS to backend services)**

**Architecture**
![alt text](https://github.com/vinodgopireddy2/iam-login/blob/master/screenshots/iam_login_service_architecture.png)

**Cloud DNS setup on GCP**
![alt text](https://github.com/vinodgopireddy2/iam-login/blob/master/screenshots/Cloud%20DNS%20configuration.png)

**Https LoadBalancer: Host&Paths config and SSL cert and termination config**
```
For rate limiting, DDos attack protection we can use ingress service provided by GCP (I have not used in my case).
```
*LoadBalancer-Host&PathRules*
![alt text](https://github.com/vinodgopireddy2/iam-login/blob/master/screenshots/Cloud%20DNS%20configuration.png)

*Cloud DNS configuration*
![alt text](https://github.com/vinodgopireddy2/iam-login/blob/master/screenshots/LoadBalancer-Host%26PathRules.png)

**Steps to setup GCP(google cloud platform) environment)**
```
- Go to google cloud console and create Kubernetes cluster
- If you do not have gcloud setup on your local machine, install it on your local machine.
- gcloud auth login
- gcloud container clusters get-credentials iam --zone us-central1-c --project iamservice-279605

- Deploy pods:
  kubectl create -f -
  Paste the pods yaml definition and press control-D
We can use 'kind: Kubernetes deployment' for pod deployments. I have used simpler ones.

- To know the external ip
  kubectl get nodes --output wide

- Open the ports on the node:
  # gcloud compute firewall-rules create kibana-node-port --allow tcp:30080 -> this is needed if we want direct http access to service, this escapes https load balancer etc.
  gcloud compute firewall-rules create kibana-node-port --allow tcp:30601

- ./gcloud-ssh-sysctl-vmmaxmapcount.sh -execute this for elk when you see "max virtual memory areas vm.max_map_count [65530] likely too low, increase to at least [262144]"

Ingress setup:
> kubectl create clusterrolebinding cluster-admin-binding \
  --clusterrole cluster-admin \
  --user $(gcloud config get-value account)
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/master/deploy/static/provider/cloud/deploy.yaml

to see cidr block
- gcloud container clusters describe iam --zone=us-central1-c | grep -i clusterIpv4CidrBlock
firewall rules
- gcloud compute firewall-rules list \
    --filter 'name~^gke-iam' \
    --format 'table(
        name,
        network,
        direction,
        sourceRanges.list():label=SRC_RANGES,
        allowed[].map().firewall_rule().list():label=ALLOW,
        targetTags.list():label=TARGET_TAGS
    )'
- make a note of target_tags from above command

- gcloud compute firewall-rules create iam-firewall \
      --action ALLOW \
      --direction INGRESS \
      --source-ranges 10.24.0.0/14 \
      --rules tcp:30080 \
      --target-tags gke-iam-7d7eeba2-node

Named-port setup for LB:
gcloud compute instance-groups unmanaged set-named-ports iam \
    --named-ports=iamservice-backend:8080
```
**API documentation**
```
https://documenter.getpostman.com/view/1363980/Szzj7cs9?version=latest#dfaa1474-aeaa-f9db-bb61-e9df65d44498
```

**REST endpoint definitions**
```
> Register api
POST https://www.identity-intuitinc.com/v1/register/
{
	"userName" : "vinodgopireddy2",
	"email": "vinodgopireddy2@gmail.com",
	"firstName": "Vinod Reddy",
	"lastName": "Gopi Reddy",
	"password" : "qwerty1A@"
}

Response:
{
    "userName": "vinodgopireddy2",
    "email": "vinodgopireddy2@gmail.com",
    "firstName": "Vinod Reddy",
    "lastName": "Gopi Reddy"
}
Response headers:
alt-svc →clear
content-length →131
content-type →application/json
date →Tue, 16 Jun 2020 02:02:37 GMT
server →Jetty(7.x.y-SNAPSHOT)
status →201
via →1.1 google



> login api
https://www.identity-intuitinc.com/v1/login
{
	"userName" : "vinodgopireddy2",
	"password" : "qwerty1A@"
}

Response:
{
    "createTime": 1592186071,
    "lastUpdatedTime": 1592186438,
    "lastLoginTime": 1592186438,
    "licenseMap": {
        "QUCIKBOOKS_SELF_EMPLOYEED": "BASIC",
        "PROSERIES_TAX": "BASIC",
        "PAYMENTS": "BASIC",
        "TURBOTAX": "BASIC",
        "MINT": "BASIC",
        "QUICKBOOKS_PAYROLL": "BASIC",
        "LACERTE_TAX": "BASIC",
        "PROCONNECT_TAX": "BASIC",
        "TSHEETS": "BASIC",
        "QUICKBOOKS_ONLINE_ACCOUNTANTS": "BASIC",
        "QUICKBOOKS": "BASIC",
        "CHECKS_AND_TAXFORMS": "BASIC",
        "TURBO": "BASIC"
    },
    "userName": "vinodgopireddy2",
    "email": "vinodgopireddy2@gmail.com",
    "firstName": "Vinod Reddy",
    "lastName": "Gopi Reddy"
}
Response headers:
access_token →A851370803F5610949AA756F851053593BC261E82162CA2BE332F8DD4E303D4B
alt-svc →clear
content-length →639
content-type →application/json
date →Tue, 16 Jun 2020 02:00:59 GMT
refresh_token →02700DB8B4C4B9195AB9A294C5E68C4EBF098C407219DA0CBD9942007AE1817C
server →Jetty(7.x.y-SNAPSHOT)
status →200
via →1.1 google
```
