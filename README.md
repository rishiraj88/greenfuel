# Green Fuel
Goal: to start a charging session with ChargePoint

## Use Case to Implement
As a driver, I want to get an immediate acknowledgement of my request to start a charging session at a station and a decision on whether I am allowed to charge my vehicle at the station.

## Actors in the Use Cases
- driver, with driver app AutoDash on a mobile device
- ChargePoint operator (CPO)
- E-Mobility service provider (EMSP) 

## 3 Apps have been developed to achieve the goal:
- autodash: driver app AutoDash
- gateway: API gateway and controller for ChargeOnTheGo
- authorization: Authorization service for ChargeOnTheGo

## The data generated out of this platform is saved and is useful to:
- driver app: for displaying charging activity , for account overview and for monthly statements;
- cloud platform: for station usage (30-day reports etc.), station status (current distribution), real-time power rating graphs and other analysis reports;
- more platforms and parties certainly.

## Cloud app for EMSP and ACL:
ACL controls who can access specific stations and offers special pricing using connections to boost driver loyalty.

A simplistic view of such an access control list (ACL) would look like:
<code>{
driver group (employee, others),
access hours (weekday officehours, nights and weekends)
}</code>

## Steps to create and publish containers:
<code>
-- while in autodash project directory
$ docker build -f local.Dockerfile -t i50729/cp-autodash:0.0.1-SNAPSHOT .
$ docker push i50729/cp-autodash:0.0.1-SNAPSHOT
</code>
<code>
-- while in gateway project directory
$ docker build -f local.Dockerfile -t i50729/cp-gateway:0.0.1-SNAPSHOT .
$ docker push i50729/cp-gateway:0.0.1-SNAPSHOT
</code>
<code>
-- while in authorization project directory
$ docker build -f local.Dockerfile -t i50729/cp-authz:0.0.1-SNAPSHOT .
$ docker push i50729/cp-authz:0.0.1-SNAPSHOT
</code>

