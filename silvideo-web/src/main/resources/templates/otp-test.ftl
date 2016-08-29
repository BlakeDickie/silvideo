<#ftl strip_whitespace = true>
<#import "common/common.ftl" as c/>
<@c.page title="Two Factor Authentication Test">
<#if testResult>
<div class="alert alert-success" role="alert">Token was valid.</div>
<#else>
<div class="alert alert-danger" role="alert">Token was not valid.</div>
</#if>
</@c.page>