<form action="/upload" enctype="multipart/form-data" method="post" accept-charset="utf-8">
    <div class="panel panel-primary">
        <div class="panel-heading">Импорт данных</div>
        <div class="panel-body">
            <div class="input-group">
                <label class="input-group-btn">
                        <span class="btn btn-primary">
                            Открыть&hellip; <input type="file" id="upload-files" name="files" style="display: none;" multiple>
                        </span>
                </label>
                <input type="text" class="form-control" readonly>
            </div>
        </div>
        <div class="panel-footer">
            <input type="submit" class="btn btn-primary" value="Загрузить">
        </div>
    </div>
</form>