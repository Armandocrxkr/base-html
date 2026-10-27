<?php

use Illuminate\Support\Facades\Route;

Route::get('/', function () {

return 'Mi primera aplicación Laravel';

});

Route::get('/hola', function () {

return 'Hola mundo desde Laravel';

});