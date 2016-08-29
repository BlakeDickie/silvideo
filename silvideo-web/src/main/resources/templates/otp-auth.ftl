<#ftl strip_whitespace = true>
<#import "common/common.ftl" as c/>
<@c.page title="Two Factor Authentication">

<div class="panel panel-primary">
    <div class="panel-heading">
        <h3 class="panel-title">Test Token Generation</h3>
        </div>
    <form action="/otp/auth/login" method="POST">
    <@c.csrf/>
        <div class="panel-body">
            <p>Please enter a Token for your Authenticator:</p>
            <input type="text" name="token"/>

        <button type="submit" class="btn btn-default">Check</button>
        </div>
    </form>
</div>

</@c.page>