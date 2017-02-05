<#ftl strip_whitespace = true>
<#import "common/common.ftl" as c/>
<@c.page title="MyList Import">
<@c.flash />
<form action="/mylist/import" method="post" enctype="multipart/form-data">
    <div class="form-group">
        <label for="file">MyList Export File</label>
        <input type="file" class="form-control" id="file" name="file" placeholder="Export File">
        </div>
    <button type="submit" class="btn btn-default">Upload File</button>
    <@c.csrf />
    </form>
</@c.page>
