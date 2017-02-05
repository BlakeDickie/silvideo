<#ftl strip_whitespace = true>
<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#macro csrf>
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</#macro>

<#macro flash>
<#if infoMessage?has_content><div class="alert alert-info" role="alert">${infoMessage}</div></#if>
<#if errorMessage?has_content><div class="alert alert-danger" role="alert">${errorMessage}</div></#if>
<#if successMessage?has_content><div class="alert alert-success" role="alert">${successMessage}</div></#if>
</#macro>

<#macro page title refresh=0>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <#if refresh?number gt 0>
            <meta http-equiv="refresh" content="${refresh}" >
        </#if>

        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <title>SilVideo - ${title}</title>

    <!-- Bootstrap -->
        <link href="/bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <link href="/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
        </head>
    <body>

        <nav class="navbar navbar-inverse navbar-static-top">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        </button>
                    <a class="navbar-brand" href="#">SilVideo</a>
                    </div>
                <div id="navbar" class="navbar-collapse collapse">
                    <ul class="nav navbar-nav navbar-right">
                        <@security.authorize access="isAuthenticated()">
                        <li><a href="/mylist/import">MyList Import</a></li>
                        <li><a href="/otp/setup">Settings</a></li>

                        <li><form action="/logout" method="POST" class="navbar-form navbar-right">
                                <button type="submit" class="btn btn-link">Logout</button>
                        <@c.csrf />
                                </form></li>
                        </@security.authorize>
                        </ul>
                    </div>
                </div>
            </nav>

        <div class="container">
        <#nested/>
            </div>

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="/bootstrap/js/bootstrap.min.js"></script>
        </body>
    </html>
</#macro>
