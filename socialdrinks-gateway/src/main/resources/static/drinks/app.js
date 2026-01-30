var app = angular.module('CocktailApp', ['ngRoute']);

app.config(function($locationProvider) {
    $locationProvider.hashPrefix('');
});

app.config(function($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: 'menu.html'
        })
        .when('/cocktails', {
            templateUrl: 'cocktail-list.html',
            controller: 'CocktailListController'
        })
        .when('/cocktails/:id', {
            templateUrl: 'cocktail-detail.html',
            controller: 'CocktailDetailController'
        })
        .when('/ingredients', {
            templateUrl: 'ingredient-list.html',
            controller: 'IngredientListController'
        })
        .when('/ingredients/:id', {
            templateUrl: 'ingredient-detail.html',
            controller: 'IngredientDetailController'
        })
        .when('/user/favorites', {
            templateUrl: 'favorites.html',
            controller: 'FavoritesController'
        })
        .when('/search', {
            templateUrl: 'search.html',
            controller: 'SearchController'
        })
        .when('/user/fridge', {
            templateUrl: 'fridge.html',
            controller: 'FridgeController'
        })
        .when('/user/cart', {
            templateUrl: 'cart.html',
            controller: 'CartController'
        })
        .when('/user/possible', {
            templateUrl: 'possible.html',
            controller: 'PossibleController'
        })
        .otherwise({
            redirectTo: '/'
        });
});

app.controller('CocktailListController', function($scope, $http) {
    $http.get('/api/cocktails')
        .then(function(response) {
            $scope.cocktails = response.data;
        })
        .catch(function(error) {
            console.error('Fehler beim Laden der Cocktails:', error);
        });
});

app.controller('CocktailDetailController', function($scope, $routeParams, $http) {
    $http.get('/api/cocktails/' + $routeParams.id)
        .then(function(response) {
            $scope.cocktail = response.data;
        })
        .catch(function(error) {
            console.error('Fehler beim Laden des Cocktails:', error);
        });
});

app.controller('IngredientListController', function($scope, $http) {
    $http.get('/api/ingredients')
        .then(function(response) {
            $scope.ingredients = response.data;
        })
        .catch(function(error) {
            console.error('Fehler beim Laden der Zutaten:', error);
        });
});

app.controller('IngredientDetailController', function($scope, $routeParams, $http) {
    $http.get('/api/ingredients/' + $routeParams.id + "/cocktails")
        .then(function(response) {
            $scope.data = response.data;
        })
        .catch(function(error) {
            console.error('Fehler beim Laden der Zutat:', error);
        });
});

app.controller('SearchController', function($scope, $http, $location) {
    $scope.query = $location.search().query || '';

    if ($scope.query) {
        // Führe die Suche durch
        $http.get('/api/cocktails/search', { params: { query: $scope.query } })
            .then(function(response) {
                $scope.results = response.data;
            })
            .catch(function(error) {
                console.error('Fehler bei der Suche:', error);
            });
    }

    $scope.search = function() {
        // Aktualisiere die URL mit dem Such-Query
        $location.search('query', $scope.query);
    };
});

app.controller('FridgeController', function($scope, $http) {
    $scope.updateFridge = function() {
        $scope.inFridge = $scope.fridge.filter(function(ingredient) {
            return ingredient.inFridge;
        });
        $scope.notInFridge = $scope.fridge.filter(function(ingredient) {
            return !ingredient.inFridge;
        });
    }

    // Hilfsfunktion zum Ändern einer Zutat
    $scope.updateIngredient = function(id, inFridge) {
        $scope.fridge.forEach(function(ingredient) {
            if (ingredient.id === id) {
                ingredient.inFridge = inFridge;
            }
        });
        $scope.updateFridge();
        $http.patch('/api/fridge/ingredients/' + id, { inFridge: inFridge })
            .then(function() {
                console.log('Zutat aktualisiert');
            })
            .catch(function(error) {
                console.error('Fehler beim Aktualisieren der Zutat:', error);
            });
    }

    // Initialisierung beim Laden der Seite
    $http.get('/api/fridge/ingredients')
        .then(function(response) {
            $scope.fridge = response.data;
            $scope.updateFridge();
        })
        .catch(function(error) {
            console.error('Fehler beim Laden der Kühlschrank-Zutaten:', error);
        });

    // Funktion zum Hinzufügen einer Zutat zum Kühlschrank
    $scope.addToFridge = function(id) {
        // Aktualisiere die inFridge-Eigenschaft der Zutat
        $scope.updateIngredient(id, true);
    };

    // Funktion zum Entfernen einer Zutat aus dem Kühlschrank
    $scope.removeFromFridge = function(id) {
        $scope.updateIngredient(id, false);
    };
});

app.controller('PossibleController', function($scope, $http) {
    $http.get('/api/fridge/possible')
        .then(function(response) {
            $scope.cocktails = response.data;
        })
        .catch(function(error) {
            console.error('Fehler beim Laden der möglichen Cocktails:', error);
        });
});

app.controller('CartController', function($scope, $http) {
    $scope.loadCart = function() {
        $http.get('/api/cart')
            .then(function(response) {
                $scope.ingredients = response.data.ingredients || [];
                $scope.cocktails = response.data.cocktails || [];
            })
            .catch(function(error) {
                console.error('Fehler beim Laden des Warenkorbs:', error);
            });
    };

    $scope.removeFromCart = function(id) {
        $http.delete('/api/cart/items/' + id)
            .then(function() {
                $scope.loadCart();
            })
            .catch(function(error) {
                console.error('Fehler beim Entfernen der Zutat:', error);
            });
    };

    $scope.addCocktailToCart = function(id) {
        $http.post('/api/cart/actions/add-cocktail', { cocktailId: id })
            .then(function() {
                $scope.loadCart();
            })
            .catch(function(error) {
                console.error('Fehler beim Hinzufügen des Cocktails:', error);
            });
    };

    $scope.clearCart = function() {
        $http.delete('/api/cart')
            .then(function() {
                $scope.loadCart();
            })
            .catch(function(error) {
                console.error('Fehler beim Leeren des Warenkorbs:', error);
            });
    };

    $scope.loadCart();
});

app.controller('FavoritesController', function($scope, $http) {
    $scope.loadFavorites = function() {
        $http.get('/api/favorites')
            .then(function(response) {
                $scope.cocktails = response.data;
            })
            .catch(function(error) {
                console.error('Fehler beim Laden der Favoriten:', error);
            });
    };

    $scope.toggleFavorite = function(id) {
        $http.post('/api/favorites/toggle', { cocktailId: id })
            .then(function() {
                $scope.loadFavorites();
            })
            .catch(function(error) {
                console.error('Fehler beim Aktualisieren des Favoriten:', error);
            });
    };

    $scope.loadFavorites();
});
