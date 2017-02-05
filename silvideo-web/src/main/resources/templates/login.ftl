<#ftl strip_whitespace = true>
<#import "common/common.ftl" as c/>
<@c.page title="Login">
<@c.flash />
<form action="/login" method="post">
    <div class="form-group">
        <label for="username">Username</label>
        <input type="text" class="form-control" id="username" name="username" placeholder="Username">
        </div>
    <div class="form-group">
        <label for="password">Password</label>
        <input type="password" class="form-control" id="password" name="password" placeholder="Password">
        </div>
    <button type="submit" class="btn btn-default">Login</button>
    <@c.csrf />
    </form>
</@c.page>
