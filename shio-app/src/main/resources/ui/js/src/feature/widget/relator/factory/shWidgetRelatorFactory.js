shioApp.factory('shWidgetRelatorFactory', [ '$uibModal',
		function($uibModal) {
			const varToString = varObj => Object.keys(varObj)[0];
			return {
				modalSelectRelatorItem : function(shPostAttr, shChildrenRelatorItem) {
					var $ctrl = this;
					return $uibModal.open({
						animation : true,
						ariaLabelledBy : 'modal-title',
						ariaDescribedBy : 'modal-body',
						templateUrl : 'template/widget/relator/relator-select.html',
						controller : 'ShWidgetRelatorSelectCtrl',
						controllerAs : varToString({ $ctrl }),
						size : null,
						appendTo : undefined,
						resolve : {
							shChildrenRelatorItem : function() {
								return shChildrenRelatorItem;
							},
							shPostAttrRelator : function() {
								return shPostAttr;
							}
						}
					});
				},
				modalAddRelatorItem : function(shPostAttr) {
					var $ctrl = this;
					return $uibModal.open({
						animation : true,
						ariaLabelledBy : 'modal-title',
						ariaDescribedBy : 'modal-body',
						templateUrl : 'template/widget/relator/relator-select.html',
						controller : 'ShWidgetRelatorAddCtrl',
						controllerAs : varToString({ $ctrl }),
						size : null,
						appendTo : undefined,
						resolve : {
							shPostAttr : function() {
								return shPostAttr;
							}
						}
					});
				}
			}
		} ]);
