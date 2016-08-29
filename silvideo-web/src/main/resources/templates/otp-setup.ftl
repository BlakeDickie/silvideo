<#ftl strip_whitespace = true>
<#import "common/common.ftl" as c/>
<@c.page title="Two Factor Authentication Setup">

<#if optEnabled>

<div class="panel panel-primary">
    <div class="panel-heading">
        <h3 class="panel-title">Turn Off Two Factor Authentication</h3>
        </div>
    <form action="/otp/setup/disable" method="POST">
        <@c.csrf/>
        <div class="panel-body">
            <p>If you no longer wish to use Two Factor Authentication for this Account
                then press press the disable button.</p>

            <p><button type="submit" class="btn btn-default">Disable</button></p>
            </div>
        </form>
    </div>


<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">Test Token Generation</h3>
        </div>
    <form action="/otp/setup/test" method="POST">
        <@c.csrf/>
        <div class="panel-body">
            <p>If you want to ensure that your token are generating correctly
                enter a current token below:</p>
            <input type="text" name="token"/>

            <button type="submit" class="btn btn-default">Check</button>
            </div>
        </form>
    </div>

<#else>

<div class="panel panel-primary">
    <div class="panel-heading">
        <h3 class="panel-title">Turn On Two Factor Authentication</h3>
        </div>
    <form action="/otp/setup/generate-key" method="POST">
        <@c.csrf/>
        <div class="panel-body">
            If you wish to use Two Factor Authentication for this Account
            then press the enable button.

            <p><button type="submit" class="btn btn-default">Enable</button></p>
            </div>
        </form>
    </div>

</#if>
</@c.page>