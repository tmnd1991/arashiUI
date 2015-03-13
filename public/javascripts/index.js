//index.js
var tree = null;
var watched = new JsSet();
var template = null;
var fromPicker = null;
var toPicker = null;

function println(x) {
    console.log(x);
}
function getBeginDate(){
    return fromPicker.datetimepicker('getDate').getTime();
}
function getEndDate(){
    return toPicker.datetimepicker('getDate').getTime();
}
function getPanelId(el) {
	return el + "_panel";
}
/**
*** Adds only the leaf nodes to the set
**/
function addNode(tree, obj, node) {
    if (!node.state.loaded){
        tree.jstree(true).load_node(node.id, function(n){
           addNode(tree, obj, n);
        });
    }
    else{
        if (node.children.length !== 0) {
            node.children.forEach(function(x) {
                var n = tree.jstree(true).get_node(x);
                if (n !== undefined)
                    addNode(tree, obj, n);
            });
        }
        else{
            obj.addElement(node.id);
        }
    }
}
/**
*** Removes all the leaf nodes from the set
**/
function removeNode(tree, obj, node) {
	obj.removeElement(node.id);
	if (node.children.length !== 0) {
		node.children.forEach(function(x) {
			var n = tree.jstree(true).get_node(x);
			if (n !== undefined)
				removeNode(tree, obj, n);
		});
	}
}
function min(x){
    var min = null
    for (k in x) {
        if (min === null)
            min = x[k];
        else
            if (min > x[k])
                min = x[k];
    }
    return min;
}
function instructions()
{
    $(document).foundation("joyride","start");
    $.cookie('JoyRide',true);
}
$(function() {
	//getting the tree div
	tree = $('#jstree_demo');
	//getting the panel template
    $(".placeholder").each(function () {
        var value = min([window.innerWidth, 840]);
        value=0.6*value;
        $(this).css("width",value);
        $(this).css("height",value);
    });
	template_graph = $('#template_graph').clone().removeAttr('id').removeAttr('style');
	template_singleValue = $('#template_singleValue').clone().removeAttr('id').removeAttr('style');
	//setup foundation
	$(document).foundation();
    if ($.cookie('JoyRide') == null)
        instructions();
	//setup jstree
	tree.jstree({
		"core": {
			"animation": 3,
			"check_callback": true,
			"themes": {
				"stripes": true
			},
            'data' : {
                'url' : function (node) {
                    return node.id === '#' ?
                        '/resources/' :
                        '/resources/'+node.id;
                }
            }
		},
		"types": {
			"#": {
				"max_children": 1,
				"max_depth": 4,
				"valid_children": ["root"]
			},
			"root": {
				"icon": "/static/3.0.9/assets/images/tree_icon.png",
				"valid_children": ["default"]
			},
			"default": {
				"valid_children": ["default", "file"]
			},
			"file": {
				"icon": "glyphicon glyphicon-file",
				"valid_children": []
			}
		},
		"checkbox": {
			"keep_selected_style": false,
			"tie_selection": false
		},
		"plugins": [
			"search", "state", "types", "wholerow", "checkbox"
		]
	});
	//setup tree check listener
	tree.on("check_node.jstree", function(event, data) {
		addNode(tree, watched, data.node);
	});
	tree.on("uncheck_node.jstree", function(event, data) {
		removeNode(tree, watched, data.node);
	});
	watched.onRemoved = function(el) {
		var toRemove = document.getElementById(getPanelId(el));
		toRemove.parentNode.removeChild(toRemove);
	};
	watched.onAdded = function(el) {
		$("#MAIN").append(newPanelForResource(el));
	};
	//setup search functions
	var to = false;
	$('#searchText').keyup(function() {
		if (to) {
			clearTimeout(to);
		}
		to = setTimeout(function() {
			var v = $('#searchText').val();
			tree.jstree(true).search(v);
		}, 250);
	});
    var dateTimePickers = $(".datetimepick");
    dateTimePickers.datetimepicker({
      maxDate: "now",
      onSelect: function(d,i){
        if(d !== i.lastVal){
            updatePanels();
        }
      }
    });
    dateTimePickers.each(
        function(){
            var _this = $(this)
            if (_this.hasClass("from")){
                fromPicker = _this;
                var d = new Date();
                d.setTime(d.getTime()-24*60*60*1000);
                _this.datepicker("setDate", d);
            }
            else{
                toPicker = _this;
                _this.datepicker("setTime", new Date());
            }
        }
    );
});
/**
*** Gets data from the resourceId
**/
function dataOfMeter(el, start, end, callback){
    var n = tree.jstree(true).get_node(el);
    var id = "http://" + n.original.parentText + n.original.text;
    $.get( "samples/", { id : id, start : start, end : end } )
        .success(function( data ) {
            var v = [];
            for(i=0;i<data.length;i++){
                v.push([i,parseFloat(data[i].value)])
            }
            callback(v);
    });
    /*
	//this is sample data
	var d1 = [];
	for (var i = 0; i < 14; i += 0.5) {
		d1.push([i, Math.sin(i)]);
	}
	var d2 = [
		[0, 3],
		[4, 8],
		[8, 5],
		[9, 13]
	];

	// A null signifies separate line segments

	var d3 = [
		[0, 12],
		[7, 12],
		null, 
		[7, 2.5],
		[12, 2.5]
	];
	return [d1,d2,d3];
	*/
}

/**
*** Creates a new html panel about the passed resource
**/
function newPanelForResource(el) {
    var n = tree.jstree(true).get_node(el);
	var r = Math.floor((Math.random() * 2)); //between 0 and 1
	var panelId = getPanelId(el);
	var toRet = null;
	if (n.original.unit !== null){
        toRet = template_graph.clone().attr('id', panelId);
        toRet.addClass("dataPanelGraph");
        updateGraphPanel(toRet);
		/*dataOfMeter(el , getBeginDate(), getEndDate(), function(d){
            $.plot(toRet.find(".placeholder"), [d]);
        });*/
	}
	else{
		toRet = template_singleValue.clone().attr('id', panelId);
        toRet.addClass("dataPanelText");
		toRet.find(".label").text("template text");
	}
	toRet.find(".titleSpan").html(n.original.parentText + n.original.text);
	return toRet;
}
/**
*** updates all the panels
**/
function updatePanels(){
    $(".dataPanelGraph").each(function(){
       updateGraphPanel($(this));
    });
    $(".dataPanelText").each(function(){
        updateTextPanel($(this));
    });
}

function updateGraphPanel(p){
    var id = p.attr("id").split("_")[0];
    dataOfMeter(id , getBeginDate(), getEndDate(), function(d){
        if (d.length != 0)
            $.plot(p.find(".placeholder"), [d]);
        else
            p.find(".placeholder").html("No samples");
    });
}

function updateTextPanel(p){
    var id = p.id.split("_")[0];
}