function getHostname(u){
	var parser = document.createElement('a');
	parser.href = u;
	return parser.hostname;
}
