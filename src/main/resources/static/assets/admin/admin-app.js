const app = angular.module("admin-app", ["ngRoute"])
app.config(function($routeProvider) {
    $routeProvider
        .when("/product",{
            templateUrl: "/assets/admin/product/index.html",
            controller: "product-ctrl"
        })
        .when("/authorize",{
            templateUrl: "/assets/admin/authority/index.html",
            controller: "authority-ctrl"
        })
        .when("/unauthorized",{
            templateUrl: "/assets/admin/authority/unauthorized.html",
            controller: "authority-ctrl"
        })
        .when("/orderall", {
            templateUrl: "/assets/admin/order/index.html",
            controller: "orderall-ctrl"
        })
        .when("/orderdetail/:id", {
            templateUrl: "/assets/admin/order/detail.html",
            controller: "orderdetail-ctrl"
        })
        .otherwise({
            templateUrl: "<h1 class='text-center'>FPT ADMIN</h1>"
        })

})