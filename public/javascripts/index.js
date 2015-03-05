//index.js
var tree = null;
var watched = new JsSet();
var template = null;

function getPanelId(el) {
	return el + "_panel";
}
/**
*** Adds only the leaf nodes to the set
**/
function addNode(tree, obj, node) {
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
			'data': getData()
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
                var d = new Date();
                d.setTime(d.getTime()-24*60*60*1000);
                console.log(d);
                _this.datepicker("setDate", d);
            }
            else{
                _this.datepicker("setTime", new Date());
            }
        }
    );
});
/**
*** Gets data from the resourceId
**/
function dataOfMeter(el){
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
}

/**
*** Creates a new html panel about the passed resource
**/
function newPanelForResource(el) {
	var r = Math.floor((Math.random() * 2)); //between 0 and 1
	var panelId = getPanelId(el);
	var toRet = null;
	if (r === 0){
		var graphData = dataOfMeter(el);
		toRet = template_graph.clone().attr('id', panelId);
		$.plot(toRet.find(".placeholder"), graphData);
	}
	else{
		toRet = template_singleValue.clone().attr('id', panelId);
		toRet.find(".label").text("template text");
	}
	toRet.find(".titleSpan").html(el);
	return toRet;
}
/**
*** updates all the panels
**/
function updatePanels(){
    console.log("updates");
}
function getData(){
    return {
        'url': "/resources"
    }/*
	return [{
				"id": "ajson1",
				"parent": "#",
				"text": "Simple root node"
			}, {
				"id": "ajson2",
				"parent": "#",
				"text": "Root node 2"
			}, {
				"id": "ajson3",
				"parent": "ajson2",
				"text": "Child 1"
			}, {
				"id": "ajson4",
				"parent": "ajson2",
				"text": "Child 2"
			}, {
				"id": "ajson5",
				"parent": "#",
				"text": "Simple root node"
			}, {
				"id": "ajson6",
				"parent": "#",
				"text": "Root node 2"
			}, {
				"id": "ajson7",
				"parent": "ajson2",
				"text": "Child 1"
			}, {
				"id": "ajson8",
				"parent": "ajson7",
				"text": "Child 2"
			}, {
				"id": "ajson9",
				"parent": "#",
				"text": "Simple root node"
			}, {
				"id": "ajson10",
				"parent": "#",
				"text": "Root node 2"
			}, {
				"id": "ajson11",
				"parent": "ajson2",
				"text": "Child 1"
			}, {
				"id": "ajson12",
				"parent": "ajson2",
				"text": "Child 2"
			}, {
				"id": "ajson13",
				"parent": "#",
				"text": "Simple root node"
			}, {
				"id": "ajson14",
				"parent": "#",
				"text": "Root node 2"
			}, {
				"id": "ajson15",
				"parent": "ajson2",
				"text": "Child 1"
			}, {
				"id": "ajson16",
				"parent": "ajson2",
				"text": "Child 2"
			}, {
				"id": "ajson17",
				"parent": "#",
				"text": "Simple root node"
			}, {
				"id": "ajson18",
				"parent": "#",
				"text": "Root node 2"
			}, {
				"id": "ajson19",
				"parent": "ajson2",
				"text": "Child 1"
			}, {
				"id": "ajson20",
				"parent": "ajson2",
				"text": "Child 2"
			}, {
				"id": "ajson21",
				"parent": "#",
				"text": "Simple root node"
			}, {
				"id": "ajson22",
				"parent": "#",
				"text": "Root node 2"
			}, {
				"id": "ajson23",
				"parent": "ajson2",
				"text": "Child 1"
			}, {
				"id": "ajson24",
				"parent": "ajson2",
				"text": "Child 2"
			}, {
				"id": "ajson25",
				"parent": "#",
				"text": "Simple root node"
			}, {
				"id": "ajson26",
				"parent": "#",
				"text": "Root node 2"
			}, {
				"id": "ajson27",
				"parent": "ajson2",
				"text": "Child 1"
			}, {
				"id": "ajson28",
				"parent": "ajson2",
				"text": "Child 2"
			}];*/
}
