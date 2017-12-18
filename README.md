# Glibrary
framework
```
<div class="row">

    <div class="col-xs-12">

        <!-- PAGE CONTENT BEGINS -->

        <form id="model-form" class="form-horizontal"  role="form" action="/admin/berkeleyCommercialCaseModel/editModel.do">

            <!-- #section:elements.form -->

<input type="hidden"  name="caseId" value="${caseId}"/>

<table class="table table-striped table-bordered table-hover dataTable no-footer" id="modulerTable">

<thead>

<td colspan="2" style="padding-left:300px">

<button name="modelBtn" type="button" class="btn add-row-trigger btn-link add-row-one"><strong><h3>添加模板</h3></strong></button>

<button class="btn btn-info" type="submit"><i class="ace-icon fa fa-check bigger-110"></i>&nbsp;保&nbsp;存&nbsp;</button>&nbsp; &nbsp; &nbsp;

<button id="back" class="btn" type="button"><i class="ace-icon fa fa-undo bigger-110"></i>&nbsp;返&nbsp;回&nbsp;</button>

</td>

            

</thead>

<tbody id="childrenList">

<c:forEach items="${result}" var="item" varStatus="current">

<input type="hidden"  name="list[${current.index}].id" value="${item.id}"/>

<tr class="children${current.index}" style="margin-top:5px;">

<td>

<table class="table table-striped table-bordered dataTable no-footer">

<tbody>

<div class="space-4"></div>

           <div class="form-group">

              <label class="col-sm-3 control-label no-padding-right" for="title"> 模板名称 </label>

               <div class="col-sm-9">

<span class="col-sm-5 no-padding block input-icon input-icon-right mr10">

<input type="text" class="width-100" maxlength="20"  name="list[${current.index}].title" placeholder="模板名称" value="${item.title}"/>

<input type="hidden"  name="item.id" value="0"/>

<i class="ace-icon fa"></i>

</span>

               </div>

           </div>

           <div class="space-4"></div>

           <div class="form-group">

               <label class="col-sm-3 control-label no-padding-right" for="image"> 模板图片 </label>

               <div class="col-sm-9">

<span class="col-sm-5 no-padding block input-icon input-icon-right mr10">

<input type="hidden" id="image${current.index}" name="list[${current.index}].image" value="${item.image}"/>

<button type="button" mult="false" sid="image${current.index}" vid="pic${current.index}-pic-view" class="btn btn-sm btn-purple btn-upload-pic" upfrom="0">

<i class="ace-icon fa fa-upload"></i> 上 传

</button>

<ul sid="pic${current.index}" id="pic${current.index}-pic-view" class="ace-thumbnails clearfix">

<a  

href="${item.imageUrl}" 

data-rel="colorbox" class="cboxElement"><img style="max-height: 150px;max-width: 150px;" alt="" 

src="${item.imageUrl}">

</a>

</ul>

</span>

               </div>

           </div>

           <div class="space-4"></div>

           <div class="form-group">

               <label class="col-sm-3 control-label no-padding-right" for="content"> 模板简介</label>

               <div class="col-sm-9">

<span class="col-sm-5 no-padding block input-icon input-icon-right mr10">

<textarea type="text" class="width-100" maxlength="255" name="list[${current.index}].content" placeholder="模板简介" >${item.content}</textarea>

<i class="ace-icon fa"></i>

</span>

               </div>

           </div>

           <div class="space-4"></div>

           <div class="form-group">

           <span class="col-sm-5 no-padding block input-icon input-icon-right mr10">

           <button name="delBtn" type="button" data-index="${current.index}" class="close del-row-trigger no-padding-right"><h4>删除模板</h4></button>

           </span>

           </div>

           </tbody>

</table>

</td>

</tr>

</c:forEach>

</tbody>

</table>

        </form>

        <!-- PAGE CONTENT ENDS -->

    </div>

    <!-- /.col -->

</div><!-- /.row -->





<script type="text/javascript">

    var scripts = [null, "/qcloud-admin/assets/js/jquery.colorbox-min.js","/qcloud-admin/assets/js/upload-img.js","/qcloud-admin/assets/js/chosen.jquery.min.js", null];

    ace.load_ajax_scripts(scripts, function () {

        //inline scripts related to this page

        var childrenListSize=${result.size()};

        $("button[name='modelBtn']").click(function(){

            var html='<tr class="children'+childrenListSize+'" style="margin-top:5px;">'+

'<td>'+

'<table class="table table-striped table-bordered dataTable no-footer">'+

'<tbody>'+

'<tr>'+

'<td class="form-group">'+

'<table class="table dataTable" style="width: 200px;">'+

'<div class="space-4"></div>'+

            '<div class="form-group">'+

               '<label class="col-sm-3 control-label no-padding-right" for="title"> 模板名称 </label>'+

                '<div class="col-sm-9">'+

'<span class="col-sm-5 no-padding block input-icon input-icon-right mr10">'+

'<input type="text" class="width-100" maxlength="20"  name="list['+childrenListSize+'].title" placeholder="模板名称" value=""/>'+

'<input type="hidden"  name="list['+childrenListSize+'].id" value="0"/>'+

'<i class="ace-icon fa"></i>'+

'</span>'+

                '</div>'+

            '</div>'+

            '<div class="space-4"></div>'+

            '<div class="form-group">'+

                '<label class="col-sm-3 control-label no-padding-right" for="image"> 模板图片 </label>'+

                '<div class="col-sm-9">'+

'<span class="col-sm-5 no-padding block input-icon input-icon-right mr10">'+

'<input type="hidden" id="image'+childrenListSize+'" name="list['+childrenListSize+'].image" value=""/>'+

'<button type="button" mult="false" sid="image'+childrenListSize+'" vid="pic'+childrenListSize+'-pic-view" class="btn btn-sm btn-purple btn-upload-pic'+childrenListSize+'" upfrom="0">'+

'<i class="ace-icon fa fa-upload"></i> 上 传'+

'</button>'+

'<ul sid="pic'+childrenListSize+'" id="pic'+childrenListSize+'-pic-view" class="ace-thumbnails clearfix">'+

'</ul>'+

'</span>'+

                '</div>'+

            '</div>'+

            '<div class="space-4"></div>'+

            '<div class="form-group">'+

                '<label class="col-sm-3 control-label no-padding-right" for="content"> 模板简介</label>'+

                '<div class="col-sm-9">'+

'<span class="col-sm-5 no-padding block input-icon input-icon-right mr10">'+

'<textarea type="text" class="width-100" maxlength="255" id="content" name="list['+childrenListSize+'].content" placeholder="模板简介" value=""></textarea>'+

'<i class="ace-icon fa"></i>'+

'</span>'+

                '</div>'+

            '</div>'+

            '<div class="space-4"></div>'+

            '<div class="form-group">'+

            '<span class="col-sm-5 no-padding block input-icon input-icon-right mr10">'+

            '<button name="delBtn" type="button" data-index="'+childrenListSize+'" class="close del-row-trigger no-padding-right"><h4>删除模板</h4></button>'+

            '</span>'+

            '</div>'+

            '</tbody>'+

'</table>'+

'</td>'+

'</tr>';

$("#childrenList").append(html);

//图片浏览

       $('.ace-thumbnails [data-rel="colorbox"]').colorbox(colorbox_params);

       $("#cboxLoadingGraphic").html("<i class='ace-icon fa fa-spinner orange'></i>");

       var btnUpload = $(".btn-upload-pic"+childrenListSize);

       delEvent(getButtonSetting(btnUpload));

       btnUpload.on('click', function () {

           var bs = getButtonSetting($(this));

           uploadDialog(bs);

       });

       childrenListSize++;

       

       $(".close").click(function(){

       var i=$(this).attr("data-index");

       $('.children'+i).remove();

       });

        });

        

        //图片浏览

        if(childrenListSize > 0) {

       $('.ace-thumbnails [data-rel="colorbox"]').colorbox(colorbox_params);

       $("#cboxLoadingGraphic").html("<i class='ace-icon fa fa-spinner orange'></i>");

       var btnUpload = $(".btn-upload-pic");

       delEvent(getButtonSetting(btnUpload));

       btnUpload.on('click', function () {

           var bs = getButtonSetting($(this));

           uploadDialog(bs);

       });

       

       $(".close").click(function(){

       var i=$(this).attr("data-index");

       $('.children'+i).remove();

       });

        }

        

       

        

        jQuery(function ($) {

            $(window)

            .off('resize.chosen')

            .on('resize.chosen', function() {

                $('.chosen-select').each(function() {

                     var $this = $(this);

                     $this.next().css({'width': $this.parent().width()});

                })

            }).trigger('resize.chosen');

                   

            //表单验证

            $("#model-form").validate({

                errorElement: 'div',

                errorClass: 'help-block col-xs-12 col-sm-reset inline',

                focusInvalid: false,

                rules: {

                    name: {

                        required: true

                    },

                    mobile: {

                        required: true

                    },



                    sort: {

                        required: true,

                        range: [0, 99999999],

                        digits: true

                    }

                },



                messages: {},



                highlight: function (e) {

                    $(e).closest('.form-group').removeClass('has-success').addClass('has-error');

                },



                success: function (e) {

                    $(e).closest('.form-group').removeClass('has-error').addClass('has-success');

                    $(e).remove();

                },



                errorPlacement: function (error, element) {

                    if (element.is('input[type=checkbox]') || element.is('input[type=radio]')) {

                        var controls = element.closest('div[class*="col-"]');

                        if (controls.find(':checkbox,:radio').length > 1) controls.append(error);

                        else error.insertAfter(element.nextAll('.lbl:eq(0)').eq(0));

                    }

                    else if (element.is('.select2')) {

                        error.insertAfter(element.siblings('[class*="select2-container"]:eq(0)'));

                    }

                    else if (element.is('.chosen-select')) {

                        error.insertAfter(element.siblings('[class*="chosen-container"]:eq(0)'));

                    }

                    else error.insertAfter(element.parent());

                },



                submitHandler: function (form) {

                    postForm('model-form');

                },

                invalidHandler: function (form) {

                }

            });

        });

    })

</script>
```
