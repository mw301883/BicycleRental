/*!
* Start Bootstrap - Shop Homepage v5.0.6 (https://startbootstrap.com/template/shop-homepage)
* Copyright 2013-2023 Start Bootstrap
* Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-shop-homepage/blob/master/LICENSE)
*/
// This file is intentionally blank
// Use this file to add JavaScript to your project
document.addEventListener('DOMContentLoaded', function () {
        // Inicjalizacja karuzeli
        var myCarousel = new bootstrap.Carousel(document.getElementById('carouselExample'), {
            interval: 5000,  // Czas trwania każdego slajdu w milisekundach (w tym przypadku 5 sekund)
            wrap: true,      // Zapętlanie karuzeli
            keyboard: true   // Obsługa klawiatury
        });
    });