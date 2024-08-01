app.controller("orderDetailCtrl", function ($scope, $http, $routeParams) {
    $scope.order = {};

    $scope.initialize = function () {
        var orderId = $routeParams.id;
        $http.get("/rest/orders/" + orderId).then(resp => {
            $scope.order = resp.data;
            $scope.order.createDate = new Date($scope.order.createDate);
        });
    };

    $scope.initialize();
});
