//jsSet
function JsSet() {
  this.innerObj = {};
  this.onChanged = function() {};
  this.onAdded = function() {};
  this.onRemoved = function() {};
  this.addElement = function(el) {
    if (this.innerObj[el] === undefined) {
      this.innerObj[el] = true;
      this.onAdded(el);
      this.onChanged("added", el);
    }
  };
  this.removeElement = function(el) {
    if (this.innerObj[el] !== undefined){
      delete this.innerObj[el];
      this.onRemoved(el);
      this.onChanged("removed", el);
    }
  };
  this.asArray = function() {
    var toRet = [];
    for (var x in this.innerObj)
      if (x !== false)
        toRet.push(x);
    return toRet;
  };
}