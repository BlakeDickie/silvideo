<#ftl strip_whitespace = true>
<#import "common/common.ftl" as c/>
<@c.page title="Settings">
<@c.flash />
<form action="/settings" method="post">
    <div class="form-group">
        <label for="anidbUsername">AniDB Username</label>
        <input type="text" class="form-control" id="anidbUsername" name="anidbUsername" placeholder="Username" value="${settings.anidbUsername}" />
        </div>
    <div class="form-group">
        <label for="anidbPassword">AniDB Password</label>
        <input type="password" class="form-control" id="anidbPassword" name="anidbPassword" placeholder="Unchanged" />
        </div>
    <button type="submit" class="btn btn-default">Save Settings</button>
    <@c.csrf />
    </form>
</@c.page>
