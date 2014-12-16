define(function (require) {
	'use strict';
	return {
		header: require('text!templates/header.tmpl'),
		login: require('text!templates/login.tmpl'),
		register: require('text!templates/register.tmpl'),		
		body: require('text!templates/body.tmpl'),
		setting: require('text!templates/setting.tmpl'),
		folderselector: require('text!templates/folderselector.tmpl'),
		settings: require('text!templates/settings.tmpl'),
		folder: require('text!templates/folder.tmpl')
	};
});