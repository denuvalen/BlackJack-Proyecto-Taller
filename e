[33mcommit 325f1e9e1972091c279a0d965a2d3686e0aac275[m[33m ([m[1;36mHEAD[m[33m -> [m[1;32mmain[m[33m, [m[1;31morigin/main[m[33m, [m[1;32mmejoras-boton-estrategia[m[33m)[m
Merge: 88733 aa5b1
Author: Miagomez1 <miayasmingomez1@gmail.com>
Date:   Thu Nov 20 08:09:33 2025 -0300

    Merge pull request #53 from Miagomez1/mia-perfil
    
    Revert "Sigo intentando subir lo del reset y movi el estilo del regis…

[33mcommit aa5b1f8734e3f0fab89b5c7efc30ed1a19c95fe8[m
Author: Mia Gomez <miayasmingomez1@gmail.com>
Date:   Thu Nov 20 07:58:59 2025 -0300

    Revert "Sigo intentando subir lo del reset y movi el estilo del registro al css"
    
    This reverts commit 761cff49fae935fb2e58e32256f15e7bc6c66a88.

[33mcommit 88733dda45d2902696c160282dc91d1609e648bd[m
Merge: 4574d 761cf
Author: Miagomez1 <miayasmingomez1@gmail.com>
Date:   Thu Nov 20 07:47:42 2025 -0300

    Merge pull request #52 from Miagomez1/mia-perfil
    
    Mia perfil

[33mcommit 761cff49fae935fb2e58e32256f15e7bc6c66a88[m
Author: Mia Gomez <miayasmingomez1@gmail.com>
Date:   Thu Nov 20 07:46:13 2025 -0300

    Sigo intentando subir lo del reset y movi el estilo del registro al css

[33mcommit 753729755cbd8d7dbc99985a31294738ec94ac66[m
Author: Mia Gomez <miayasmingomez1@gmail.com>
Date:   Thu Nov 20 07:40:38 2025 -0300

    Borre el reset porque daba error 500

[33mcommit cb1f24ceb6bb388f6758a4d44a1e5a2db5c78072[m
Author: Mia Gomez <miayasmingomez1@gmail.com>
Date:   Thu Nov 20 07:38:45 2025 -0300

    Borre el reset porque rompia con error 500

[33mcommit 4574dd3521f8cdf3237dd1a9b7af7d0d8067a64f[m
Author: FacundoThibaut1010 <thibautfacundo7@gmail.com>
Date:   Thu Nov 20 00:20:36 2025 -0300

    inplementacion API Mercado Pago

[33mcommit 48fee5f975573925c413faeb0a98a5da6f4a3a30[m
Merge: 86e76 c2edb
Author: FacundoThibaut1010 <thibautfacundo7@gmail.com>
Date:   Thu Nov 20 00:04:04 2025 -0300

    Implementacion Api Mercado Pago

[33mcommit 86e76d75881fa13adfeafe13868e7d4a378b2a19[m
Author: FacundoThibaut1010 <thibautfacundo7@gmail.com>
Date:   Wed Nov 19 23:56:58 2025 -0300

    integracion API Mercado Pago

[33mcommit c2edbc2947b31f495c82448b5c8193aaa8038ab2[m
Merge: 33c71 f2ef9
Author: Valentina <valenvdz7@gmail.com>
Date:   Wed Nov 19 21:36:44 2025 -0300

    Merge pull request #51 from Miagomez1/mejoras-finales
    
    Modificacion estilos del juego:

[33mcommit f2ef9cf2f6a71d2906f564ae654075386ae9cedf[m
Author: Valentina <valenvdz7@gmail.com>
Date:   Wed Nov 19 21:31:06 2025 -0300

    Modificacion estilos del juego:
    Incorporacion de tipografia "poppins"
    correccion de medidas en los elementos
    cambio de colores
    cambio de grosor de fuentes
    Se amplia el espaciado entre elementos
    Se modifica el tamaño de la carta oculta para que tenga mismo tamaño que las demas cartas
    Modificacion de espacio en elementos

[33mcommit 33c7106f74f674268b976fc1eb0d20bafbc83541[m
Merge: f8dc5 23283
Author: Valentina <valenvdz7@gmail.com>
Date:   Wed Nov 19 17:53:35 2025 -0300

    Merge pull request #50 from Miagomez1/mejoras-finales
    
    Se agrega logica de negocio que al dividir la partida, se pueda divid…

[33mcommit 23283c5208b5cd2e1b137efc23b3e8e415d9a829[m
Author: Valentina <valenvdz7@gmail.com>
Date:   Wed Nov 19 17:48:57 2025 -0300

    Se agrega logica de negocio que al dividir la partida, se pueda dividir cuando aparezca la combinacion de dos cartas especiales "K,Q,J".
    Antes solo evaluaba las cartas con valor numeric, pero dejaba afuera las cartas especiales que teoricamente cumplen la condicion de mismo valor numerico.

[33mcommit f8dc59342beb9b4ea4b475c84ca3dcaaa6fa3f2c[m
Merge: 9ed27 05d35
Author: Valentina <valenvdz7@gmail.com>
Date:   Wed Nov 19 13:01:42 2025 -0300

    Merge pull request #49 from Miagomez1/mejoras-finales
    
    Mejoras finales

[33mcommit 05d35abfc18397cea7a17e7042a694775c41f7f3[m
Merge: fb1cd 9ed27
Author: Valentina <valenvdz7@gmail.com>
Date:   Wed Nov 19 12:59:17 2025 -0300

    Merge branch 'main' into mejoras-finales

[33mcommit fb1cd6e059d8f74fc8beaafc8778149f325f65e9[m
Author: Valentina <valenvdz7@gmail.com>
Date:   Wed Nov 19 12:04:53 2025 -0300

    Se soluciono en "pararse" el mensaje del resultado al finalizar la partida, ya que antes tenia problemas en la evaluacion y persistencia de los datos.
    Se mejoro el metodo del controladorPartida relacionado con pararse.
    Se implementa un metodo que gestiona las responsabilidades que conlleva el "pararse"
    En el reparto de cartas final del crupier se evalua con un ciclo while las cartas que le corresponde tener y mostrar segun su puntaje.

[33mcommit 9ed275a92984a33e4ecb261f613b57179760ae30[m
Merge: d9ad5 f7e30
Author: Miagomez1 <miayasmingomez1@gmail.com>
Date:   Tue Nov 18 20:58:14 2025 -0300

    Merge pull request #48 from Miagomez1/mia-perfil
    
    Hice el codigo mas prolijo, corregi errores y saque cosas innecesaria…

[33mcommit f7e30b4d6add74701fbb6915f73941b07f43050b[m
Author: Mia Gomez <miayasmingomez1@gmail.com>
Date:   Tue Nov 18 20:54:49 2025 -0300

    Hice el codigo mas prolijo, corregi errores y saque cosas innecesarias (incluidos todos los comentarios)

[33mcommit d9ad5bbfb656cc35c8ca98384a519e19e070ffd9[m
Merge: e48b7 d9577
Author: Miagomez1 <miayasmingomez1@gmail.com>
Date:   Tue Nov 18 20:21:42 2025 -0300

    Merge pull request #47 from Miagomez1/mia-perfil
    
    Mia perfil

[33mcommit d9577ff8f7b0e2c72f25b0c3897bdb4169e0a669[m
Merge: d0579 e48b7
Author: Mia Gomez <miayasmingomez1@gmail.com>
Date:   Tue Nov 18 20:19:03 2025 -0300

    Merge branch 'main' into mia-perfil

[33mcommit d0579178abc1281b9100d39991f37551532516b6[m
Author: Mia Gomez <miayasmingomez1@gmail.com>
Date:   Tue Nov 18 20:18:27 2025 -0300

    Mini cambio en la vista del juego pero porque creo que ya esta bien asi

[33mcommit e48b7c9c68f374c761c8566c837496ed2d474100[m
Merge: 754ab 185f5
Author: Valentina <valenvdz7@gmail.com>
Date:   Tue Nov 18 13:07:12 2025 -0300

    Merge pull request #46 from Miagomez1/mejoras-finales
    
    Modificaciones en la vista del HOME:

[33mcommit 185f5af9268b8e2609f4973bad0d7be3327cbffa[m
Author: Valentina <valenvdz7@gmail.com>
Date:   Tue Nov 18 13:02:25 2025 -0300

    Modificaciones en la vista del HOME:
    * Aplicacion de tipografia Anton y Poppins en elementos determinados
    *Cambio de color en texto: botones
    *Mas espaciado entre botones*Se retira el icono del usuario en el nav
    *Se plantea mismo ancho para botones
    *Se soluciona ancho de vista

[33mcommit 754ab9466bf012e0d757f6915c68c8c9fae6e9c6[m
Merge: 942bc 32254
Author: Valentina <valenvdz7@gmail.com>
Date:   Tue Nov 18 09:23:37 2025 -0300

    Merge pull request #45 from Miagomez1/mejoras-finales
    
    Se incorporo el footer a el login que se encontraba sin sus estilos y…

[33mcommit 32254ef0d32be99e75f7976270eb3195e5d2ca68[m
Author: Valentina <valenvdz7@gmail.com>
Date:   Tue Nov 18 09:19:07 2025 -0300

    Se incorporo el footer a el login que se encontraba sin sus estilos y con sintaxis inadecuada

[33mcommit 942bc8142645d978e9bee44f67b31aaa5755da69[m
Merge: da305 83e35
Author: Valentina <valenvdz7@gmail.com>
Date:   Mon Nov 17 18:10:22 2025 -0300

    Merge pull request #44 from Miagomez1/mejoras-finales
    
    Modificaciones en el login con el objetivo de acercarnos al boceto pl…

[33mcommit 83e35894f79517a777b99e5af49110f0dbff2901[m
Author: Valentina <valenvdz7@gmail.com>
Date:   Mon Nov 17 18:07:02 2025 -0300

    Modificaciones en el login con el objetivo de acercarnos al boceto planteado en la primer review:
    Inclusion de tipografía Poppins
    El ingreso de datos input es de izquierda a derecha
    Se agrandaron los input
    Se cambió el texto de input a email y contraseña
    Se modificaron tamaños de tipografías y estilos
    Se cambió el botón de abrir cuenta por una etiqueta a
    Se agregó borde superior para separar el inicio del posible registro
    Se cambiaron bordes del contenedor principal

[33mcommit da305eaa590a4c05054ed55f768c9357acf06d2e[m
Merge: 508e8 e74e8
Author: Valentina <valenvdz7@gmail.com>
Date:   Mon Nov 17 13:56:57 2025 -0300

    Me