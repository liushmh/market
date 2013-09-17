function StockCtrl($scope, $http) {
	$scope.stockList = {};

	$scope.init = function() {
		var data = new Object();
		data.cl = '600880';
		$http.get('rt?cl=600880,600881').success(
				function(data, status, headers, config) {
					for ( var s in data) {
						$scope.stockList[s] = data[s];
					}

				}).error(function(data, status, headers, config) {
			$scope.status = status;
		});
	};

	// $scope.addTodo = function() {
	// $scope.todos.push({text:$scope.todoText, done:false});
	// $scope.todoText = '';
	// };
	// 
	$scope.remaining = function() {
		var count = 0;
		angular.forEach($scope.stockList, function(stock) {
			count += 1;
		});
		return count;
	};
	// 
	// $scope.archive = function() {
	// var oldTodos = $scope.todos;
	// $scope.todos = [];
	// angular.forEach(oldTodos, function(todo) {
	// if (!todo.done) $scope.todos.push(todo);
	// });
	// };
}