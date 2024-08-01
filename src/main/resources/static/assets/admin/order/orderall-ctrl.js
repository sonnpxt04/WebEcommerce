app.controller("orderall-ctrl", function ($scope, $http) {
    $scope.items = [];

    $scope.initialize = function () {
        // Load orders
        $http.get("/rest/orders").then(resp => {
            $scope.items = resp.data;
            $scope.items.forEach(item => {
                item.createDate = new Date(item.createDate); // Convert date to JavaScript Date object
            });
        });
    };

    // Initialization
    $scope.initialize();
});
