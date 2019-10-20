var app = angular.module("App", ['ngRoute', 'ngResource']);

app.config(function ($routeProvider, $httpProvider) {
    $routeProvider
        .when('/administrate', {templateUrl: 'view/adminPage.html'})
        .when('/place', {templateUrl: 'view/place.html'})
        .when('/review', {templateUrl: 'view/review.html'})
        .when('/login', {templateUrl: 'view/login.html'})
        .when('/registration', {templateUrl: 'view/registration.html'})
        .when('/', {
            templateUrl: 'view/home.html',
            controller: 'reviewController'
        })
        .otherwise(
            {redirectTo: '/'}
        );

    $httpProvider.interceptors.push(function ($q, $window) {
        return {
            'request': function (config) {
                config.headers['Authorization'] = $window.localStorage.getItem('token');
                return config;
            }
        };
    });
});


function getPage($http, $scope) {
    return function (page) {
        $http({
            url: '/reviews/pages/' + page,
            method: "GET",
            params: {filter: $scope.searchFilter, cityName: $scope.searchCityName}
        }).then(function (response) {
            $scope.reviews = response.data.content;
            $scope.lastPage = response.data.totalPages;
            $scope.curPage = page;
        }, function (error) {
            console.log(error);
        });
    }
}

app.controller("reviewController", function ($scope, $http) {
    $scope.reviews = [];
    $scope.curPage = 1;
    $scope.searchFilter = "";
    $scope.searchCityName = "";
    $scope.goToPage = getPage($http, $scope);
    $scope.goToPage($scope.curPage);

    // for style purpose
    $scope.getStyle = function (n) {
        let styles = ['mega', 'hours', 'lime', 'orange', 'sky', 'atlas'];
        return styles[n];
    };
    $scope.getRandom = function(){
        return Math.floor((Math.random()*6));
    };

});


function getFindCities($scope) {
    return function () {
        for (let i = 0; i < $scope.countries.length; i++) {
            console.log(i);
            if ($scope.countries[i].id === Number($scope.model.countryId)) {
                $scope.cities = $scope.countries[i].cities;
                break;
            }
        }
    };
}

app.controller("placePageController", function ($scope, $http, $window) {
    $scope.countries = [];
    $scope.types = [];
    $scope.cities = [];
    $scope.model = {
        "cityId": 0,
        "countryId": 0,
        "typeId": 0,
        "name": ''
    };

    getCountries($http, $scope);
    getTypes($http, $scope);
    $scope.addPlace = addModelObjectByPOST($http, $window, $scope, $scope.model, "/places");
    $scope.findCities = getFindCities($scope);
});


app.controller("loginPageController", function ($scope, $http, $window) {
    $scope.username = undefined;
    $scope.password = undefined;
    $scope.signIn = getSignIn($scope, $http, $window);
});

function tryToReg($scope, $http, $window) {
    return function () {
        $scope.passError = undefined;
        $scope.regErrors = undefined;
        if ($scope.pass1 !== $scope.pass2) {
            $scope.passError = "Passwords are different !";
            return;
        }
        $http.post('/person',
            {
                "name": $scope.name,
                "password": $scope.pass1
            })
            .then(function (ok) {
                $window.location.href = '/';
            }, function (error) {
                if (error.data) {
                    $scope.regErrors = error.data;
                }
            });
    }
}

app.controller("registrController", function ($scope, $http, $window) {
    $scope.name = undefined;
    $scope.pass1 = undefined;
    $scope.pass2 = undefined;

    $scope.addPerson = tryToReg($scope, $http, $window);
});

app.controller("navbarController", function ($scope, $http, $window) {
    $scope.personName = $window.localStorage.getItem('personName');
    $scope.logOut = getLogOut($scope, $window);
});

app.controller("reviewPageController", function ($scope, $http, $window) {
    $scope.cities = [];
    $scope.model = {
        "placeId": 0,
        "rating": 1,
        "comment": ''
    };

    getCities($http, $scope);
    $scope.addReview = addModelObjectByPOST($http, $window, $scope, $scope.model, "/reviews");
});

app.controller("adminPageController", function ($scope, $http, $window) {
    $scope.countries = [];
    $scope.cities = [];
    $scope.types = [];
    $scope.typeModel = {
        "name": ''
    };
    $scope.cityModel = {
        "countryId": 0,
        "name": ''
    };
    $scope.countryModel = {
        "name": '',
        "code": ''
    };

    getCountries($http, $scope);
    getCities($http, $scope);
    getTypes($http, $scope);
    $scope.postCountry = addCountry($http, $scope.countryModel, $window, $scope);
    $scope.postCity = addCity($http, $window, $scope, $scope.cityModel);
    $scope.postType = addType($http, $window, $scope, $scope.typeModel);
    $scope.deleteCountry = deleteRequestById($http, $window, '/countries/');
    $scope.deleteCity = deleteRequestById($http, $window, '/cities/');
    $scope.deleteType = deleteRequestById($http, $window, '/types/');
});


function addModelObjectByPOST($http, $window, $scope, model, url) {
    return function () {
        $scope.postErrors = undefined;
        $http({
            method: "POST",
            url: url,
            data: model,
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(function (res) {
                $window.location.href = '/';
            },
            function (error) {
                console.log("Error: " + error.status + " : " + error.data);
                if (error.data) {
                    $scope.postErrors = error.data;
                }
            });
    };
}


function getCountries($http, $scope) {
    $http.get('/countries')
        .then(function (response) {
            $scope.countries = response.data.data;
        }, function (error) {
            console.log(error);
        });
}

function getTypes($http, $scope) {
    $http.get('/types')
        .then(function (response) {
            $scope.types = response.data.data;
        }, function (error) {
            console.log(error);
        });
}

function getCities($http, $scope) {
    $http.get('/cities')
        .then(function (response) {
                $scope.cities = response.data.data;
            }, function (error) {
                console.log(error);
            }
        );
}


function addCountry($http, country, $window, $scope) {
    return function () {
        $scope.nameErr = undefined;
        $scope.codeErr = undefined;
        $http({
            method: "POST",
            url: "/countries",
            data: country,
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(function (ok) {
                $window.location.reload()
            },
            function (error) {
                console.log("Error: " + error.status + " : " + error.data);
                if (error.data.name) {
                    $scope.nameErr = error.data.name;
                }
                if (error.data.code) {
                    $scope.codeErr = error.data.code;
                }
                console.log(error.data.name);
                console.log(error.data.code);
            });
    };
}

function addCity($http, $window, $scope, city) {
    return function () {
        $scope.cityErr = undefined;
        $http({
            method: "POST",
            url: "/cities",
            data: city,
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(function (ok) {
                $window.location.reload()
            },
            function (error) {
                console.log("Error: " + error.status + " : " + error.data);
                if (error.data.name) {
                    $scope.cityErr = error.data.name;
                }
            });
    };
}

function addType($http, $window, $scope, type) {
    return function () {
        $scope.typeErr = undefined;
        $http({
            method: "POST",
            url: "/types",
            data: type,
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(function (ok) {
                $window.location.reload()
            },
            function (error) {
                console.log("Error: " + error.status + " : " + error.data);
                if (error.data.name) {
                    $scope.typeErr = error.data.name;
                }
            });
    };
}

function deleteRequestById($http, $window, url) {
    return function (id) {
        $http({
            method: 'DELETE',
            url: url + id
        }).then(function (value) {
            $window.location.reload();
        }, function (error) {
            console.log("Error: " + error.status + " : " + error.data);
        });
    };
}

app.filter('range', function () {
    return function (input, min, max) {
        min = parseInt(min);
        max = parseInt(max);
        for (var i = min; i <= max; i++)
            input.push(i);
        return input;
    };
});

function getSignIn($scope, $http, $window) {
    return function () {
        $http({
            url: '/login',
            method: "POST",
            data: {
                "name": $scope.username,
                "password": $scope.password
            },
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(function (ok) {
            $window.localStorage.setItem('token', ok.headers('Authorization'));
            $window.localStorage.setItem('personName', $scope.username);
            $window.location.href = '/';
        }, function (error) {
            console.log(error.data);
            alert(error.data.message);
        });
    };
}

function getLogOut($scope, $window) {
    return function () {
        $scope.name = undefined;
        $window.localStorage.setItem('token', '');
        $window.localStorage.setItem('personName', '');
        $window.location.reload();
    };
}

function getCardStyle() {
    let styles = ['mega', 'hours', 'lime', 'orange', 'sky', 'atlas'];
    let n = Math.floor(Math.random() * 6);
    console.log(styles[n]);
    return styles[n];
}