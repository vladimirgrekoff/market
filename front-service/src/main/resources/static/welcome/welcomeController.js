app.controller("welcomeController", function($rootScope, $scope, $http, $location, $localStorage, $templateCache) {
//    const contextPath = 'http://localhost:8189/market-core/api/v1';
    const contextPath = 'http://localhost:5555/core/api/v1';

    $scope.$on('routeChangeStart', function(event, next, current) {
        if (typeof(current) != 'undefined') {
            $templateCache.remove(next.templateUrl);
        }
    });

    $scope.defaultNumber = 5;

    if($localStorage.productsOnPage){
        $scope.applyValue = $localStorage.productsOnPage;
    }else{
        $scope.applyValue = $scope.defaultNumber;
    };

    $scope.applyValue;

    $scope.applyNumberProducts = function(){
        $localStorage.productsOnPage = $scope.numberProduct;
        $scope.loadPageProducts(0, true, false);
    };

    $scope.checkLimit = function(){
//        if(limit == 5){
        if($localStorage.productsOnPage){
            if($localStorage.productsOnPage != undefined && $localStorage.productsOnPage != null) {
                limit = $localStorage.productsOnPage;
                $scope.defaultNumber = limit;
            }
        } else{
            limit = $scope.defaultNumber;
        }
        return limit;
    };

    $scope.loadPageProducts = function (offset, first, last) {
        limit = $scope.checkLimit();
        $http({
            url: contextPath + '/products/pages',
            method: 'GET',
            params: {
                min_price: $scope.filter ? $scope.filter.min_price : null,
                max_price: $scope.filter ? $scope.filter.max_price : null,
                part_title: $scope.filter ? $scope.filter.part_title : null,
                offset: offset,
                limit: limit,
                first: first,
                last: last
            }
        }).then(function (response) {
                $scope.ProductsList = response.data.content;
        });
    };

    $scope.loadAllProducts = function () {
        $http.get(contextPath + '/products/all')
            .then(function (response) {
                $scope.ProductsList = response.data;
            });
    };


    $scope.showLoginPage = function () {
        $location.path('login');
    };

    $scope.showRegistrationPage = function () {
        $location.path('registration');
    };


    $scope.tryLogout = function() {
        if($rootScope.logout){
            $rootScope.logout();
        } else {
            $location.path('welcome')
        }
    };



    $scope.loadPageProducts();

});