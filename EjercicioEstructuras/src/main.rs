struct Persona {
    nombre: String,
    edad: u32,
    sexo: u8, // 1 = hombre, 0 = mujer
    altura: f64,
    peso: f64,
}

fn promedio(datos: &[f64]) -> Option<f64> {
    if datos.is_empty() {
        return None;
    }
    let suma: f64 = datos.iter().sum();
    Some(suma / datos.len() as f64)
}

fn valor_minimo(datos: &[f64]) -> Option<f64> {
    datos.iter().copied().reduce(f64::min)
}

fn valor_maximo(datos: &[f64]) -> Option<f64> {
    datos.iter().copied().reduce(f64::max)
}

fn contar_hombres(personas: &[Persona]) -> usize {
    personas.iter().filter(|p| p.sexo == 1).count()
}

fn contar_mujeres(personas: &[Persona]) -> usize {
    personas.iter().filter(|p| p.sexo == 0).count()
}

fn porcentaje(cantidad: usize, total: usize) -> f64 {
    if total == 0 {
        return 0.0;
    }
    (cantidad as f64 / total as f64) * 100.0
}

fn promedio_peso_sexo(personas: &[Persona], sexo: u8) -> Option<f64> {
    let pesos: Vec<f64> = personas
        .iter()
        .filter(|persona| persona.sexo == sexo)
        .map(|persona| persona.peso)
        .collect();
    promedio(&pesos)
}

fn promedio_edad_sexo(personas: &[Persona], sexo: u8) -> Option<f64> {
    let edades: Vec<f64> = personas
        .iter()
        .filter(|persona| persona.sexo == sexo)
        .map(|persona| persona.edad as f64)
        .collect();
    promedio(&edades)
}

fn personas_mayores_30(personas: &[Persona]) -> Vec<&Persona> {
    personas
        .iter()
        .filter(|persona| persona.edad > 30)
        .collect()
}

fn peso_total(personas: &[Persona]) -> f64 {
    personas
        .iter()
        .map(|persona| persona.peso)
        .fold(0.0, |acumulador, peso| acumulador + peso)
}

fn calcular_imc(persona: &Persona) -> Option<f64> {
    if persona.altura <= 0.0 {
        return None;
    }
    Some(persona.peso / (persona.altura * persona.altura))
}

fn clasificar_imc(imc: f64) -> &'static str {
    if imc < 18.5 {
        "Bajo peso"
    } else if imc < 25.0 {
        "Peso normal"
    } else if imc < 30.0 {
        "Sobrepeso"
    } else {
        "Obesidad"
    }
}

fn texto_opcion(valor: Option<f64>) -> String {
    match valor {
        Some(v) => format!("{:.2}", v),
        None => String::from("N/A"),
    }
}

fn mostrar_personas(personas: &[Persona]) {
    println!("LISTADO DE PERSONAS");
    println!(
        "{:<4} {:<15} {:<8} {:<10} {:<10} {:<10}",
        "No.", "Nombre", "Edad", "Sexo", "Altura", "Peso"
    );

    for (i, persona) in personas.iter().enumerate() {
        let sexo = if persona.sexo == 1 { "Hombre" } else { "Mujer" };
        println!(
            "{:<4} {:<15} {:<8} {:<10} {:<10.2} {:<10.2}",
            i + 1,
            persona.nombre,
            persona.edad,
            sexo,
            persona.altura,
            persona.peso
        );
    }
    println!();
}

fn mostrar_resumen(personas: &[Persona]) {
    let total = personas.len();

    if total == 0 {
        println!("No existen datos para analizar.");
        return;
    }

    // A. Hombres y mujeres
    let hombres = contar_hombres(personas);
    let mujeres = contar_mujeres(personas);

    // B. Porcentajes
    let pct_hombres = porcentaje(hombres, total);
    let pct_mujeres = porcentaje(mujeres, total);

    // C. Edades, alturas y pesos
    let edades: Vec<f64> = personas.iter().map(|p| p.edad as f64).collect();
    let alturas: Vec<f64> = personas.iter().map(|p| p.altura).collect();
    let pesos: Vec<f64> = personas.iter().map(|p| p.peso).collect();

    // D. Promedios
    let prom_edad = promedio(&edades);
    let prom_altura = promedio(&alturas);
    let prom_peso = promedio(&pesos);

    // E. Mínimos y máximos
    let min_edad = valor_minimo(&edades);
    let max_edad = valor_maximo(&edades);
    let min_altura = valor_minimo(&alturas);
    let max_altura = valor_maximo(&alturas);
    let min_peso = valor_minimo(&pesos);
    let max_peso = valor_maximo(&pesos);

    // F. Promedios por sexo
    let peso_h = promedio_peso_sexo(personas, 1);
    let peso_m = promedio_peso_sexo(personas, 0);
    let edad_h = promedio_edad_sexo(personas, 1);
    let edad_m = promedio_edad_sexo(personas, 0);

    // G. Acumulaciones
    let total_peso = peso_total(personas);
    let total_edad = personas.iter().map(|p| p.edad).fold(0, |a, e| a + e);
    let mayores = personas_mayores_30(personas);

    // Personas destacadas
    let mas_pesada = personas
        .iter()
        .max_by(|a, b| a.peso.partial_cmp(&b.peso).unwrap());
    let mas_baja = personas
        .iter()
        .min_by(|a, b| a.altura.partial_cmp(&b.altura).unwrap());

    // H. Mostrar resultados
    println!("1. DISTRIBUCION DE LA POBLACION");
    println!("Total de personas: {}", total);
    println!("Hombres: {} ({:.2}%)", hombres, pct_hombres);
    println!("Mujeres: {} ({:.2}%)", mujeres, pct_mujeres);
    println!();

    println!("2. PROMEDIOS GENERALES");
    println!("Edad promedio: {} años", texto_opcion(prom_edad));
    println!("Altura promedio: {} m", texto_opcion(prom_altura));
    println!("Peso promedio: {} kg", texto_opcion(prom_peso));
    println!();

    println!("3. VALORES MINIMOS Y MAXIMOS");
    println!(
        "Edad: min {} / max {}",
        texto_opcion(min_edad),
        texto_opcion(max_edad)
    );
    println!(
        "Altura: min {} / max {}",
        texto_opcion(min_altura),
        texto_opcion(max_altura)
    );
    println!(
        "Peso: min {} / max {}",
        texto_opcion(min_peso),
        texto_opcion(max_peso)
    );
    println!();

    println!("4. PROMEDIOS SEGUN EL SEXO");
    println!(
        "Hombres - edad: {} años, peso: {} kg",
        texto_opcion(edad_h),
        texto_opcion(peso_h)
    );
    println!(
        "Mujeres - edad: {} años, peso: {} kg",
        texto_opcion(edad_m),
        texto_opcion(peso_m)
    );
    println!();

    println!("5. ACUMULACIONES GENERALES");
    println!("Peso total acumulado: {:.2} kg", total_peso);
    println!("Suma de edades: {} años", total_edad);
    println!("Personas mayores de 30 años: {}", mayores.len());
    for p in mayores.iter() {
        println!("  - {} ({} años)", p.nombre, p.edad);
    }
    println!();

    println!("6. PERSONAS DESTACADAS");
    if let Some(p) = mas_pesada {
        println!("Mayor peso: {} ({:.2} kg)", p.nombre, p.peso);
    }
    if let Some(p) = mas_baja {
        println!("Menor altura: {} ({:.2} m)", p.nombre, p.altura);
    }
    println!();

    println!("7. ANALISIS POR RANGOS DE EDAD");
    let rangos: [(&str, u32, u32); 4] = [
        ("18 a 25 años", 18, 25),
        ("26 a 35 años", 26, 35),
        ("36 a 45 años", 36, 45),
        ("46 años o más", 46, u32::MAX),
    ];
    for (etiqueta, desde, hasta) in rangos.iter() {
        let cantidad = personas
            .iter()
            .filter(|p| p.edad >= *desde && p.edad <= *hasta)
            .count();
        println!(
            "{}: {} ({:.2}%)",
            etiqueta,
            cantidad,
            porcentaje(cantidad, total)
        );
    }
    println!();
}

fn mostrar_imc(personas: &[Persona]) {
    println!("ANALISIS DEL INDICE DE MASA CORPORAL (IMC)");
    println!("{:<15} {:<10} {:<15}", "Nombre", "IMC", "Clasificacion");

    for persona in personas.iter() {
        match calcular_imc(persona) {
            Some(imc) => println!(
                "{:<15} {:<10.2} {:<15}",
                persona.nombre,
                imc,
                clasificar_imc(imc)
            ),
            None => println!(
                "{:<15} {:<10} {:<15}",
                persona.nombre, "N/A", "Altura inválida"
            ),
        }
    }
    println!();
}

fn mostrar_retos(personas: &[Persona]) {
    let total = personas.len();
    println!("ACTIVIDADES DE AMPLIACION");

    // Reto 1: nuevos filtros
    let mayores_40: Vec<&str> = personas
        .iter()
        .filter(|p| p.edad > 40)
        .map(|p| p.nombre.as_str())
        .collect();
    println!("Mayores de 40 años: {}", mayores_40.join(", "));

    let pesos: Vec<f64> = personas.iter().map(|p| p.peso).collect();
    let prom_peso = promedio(&pesos).unwrap_or(0.0);
    let sobre_promedio: Vec<&str> = personas
        .iter()
        .filter(|p| p.peso > prom_peso)
        .map(|p| p.nombre.as_str())
        .collect();
    println!(
        "Peso mayor al promedio general: {}",
        sobre_promedio.join(", ")
    );

    let altos: Vec<&str> = personas
        .iter()
        .filter(|p| p.altura > 1.70)
        .map(|p| p.nombre.as_str())
        .collect();
    println!("Altura superior a 1.70 m: {}", altos.join(", "));

    let jovenes_mujeres: Vec<&str> = personas
        .iter()
        .filter(|p| p.edad < 25 && p.sexo == 0)
        .map(|p| p.nombre.as_str())
        .collect();
    println!("Mujeres menores de 25 años: {}", jovenes_mujeres.join(", "));

    // Reto 2: nuevas estadísticas
    println!(
        "Promedio de edad - hombres: {} / mujeres: {}",
        texto_opcion(promedio_edad_sexo(personas, 1)),
        texto_opcion(promedio_edad_sexo(personas, 0))
    );
    let pesados = personas.iter().filter(|p| p.peso > 80.0).count();
    println!("Personas con peso superior a 80 kg: {}", pesados);
    let cant_mayores_30 = personas.iter().filter(|p| p.edad > 30).count();
    println!(
        "Porcentaje de mayores de 30 años: {:.2}%",
        porcentaje(cant_mayores_30, total)
    );
    let imcs: Vec<f64> = personas.iter().filter_map(calcular_imc).collect();
    println!("Promedio de IMC: {}", texto_opcion(promedio(&imcs)));

    // Reto 3: composición
    let nombres_mayores_30: Vec<&String> = personas
        .iter()
        .filter(|p| p.edad > 30)
        .map(|p| &p.nombre)
        .collect();
    println!(
        "Nombres de mayores de 30 (encadenado): {:?}",
        nombres_mayores_30
    );

    // Reto 4: for mutable vs fold
    let mut total_for = 0.0;
    for p in personas.iter() {
        total_for += p.peso;
    }
    let total_fold = personas.iter().map(|p| p.peso).fold(0.0, |a, p| a + p);
    println!("Peso total con for: {:.2} kg", total_for);
    println!("Peso total con fold: {:.2} kg", total_fold);
    println!();
}

fn main() {
    let personas: Vec<Persona> = vec![
        Persona {
            nombre: String::from("Carlos"),
            edad: 25,
            sexo: 1,
            altura: 1.72,
            peso: 72.5,
        },
        Persona {
            nombre: String::from("Maria"),
            edad: 30,
            sexo: 0,
            altura: 1.60,
            peso: 58.0,
        },
        Persona {
            nombre: String::from("Jose"),
            edad: 42,
            sexo: 1,
            altura: 1.78,
            peso: 85.0,
        },
        Persona {
            nombre: String::from("Ana"),
            edad: 22,
            sexo: 0,
            altura: 1.65,
            peso: 55.5,
        },
        Persona {
            nombre: String::from("Luis"),
            edad: 35,
            sexo: 1,
            altura: 1.80,
            peso: 92.3,
        },
        Persona {
            nombre: String::from("Sofia"),
            edad: 28,
            sexo: 0,
            altura: 1.58,
            peso: 49.0,
        },
        Persona {
            nombre: String::from("Pedro"),
            edad: 51,
            sexo: 1,
            altura: 1.70,
            peso: 88.4,
        },
        Persona {
            nombre: String::from("Lucia"),
            edad: 19,
            sexo: 0,
            altura: 1.62,
            peso: 60.2,
        },
        Persona {
            nombre: String::from("Miguel"),
            edad: 38,
            sexo: 1,
            altura: 1.75,
            peso: 79.0,
        },
        Persona {
            nombre: String::from("Carmen"),
            edad: 45,
            sexo: 0,
            altura: 1.55,
            peso: 81.5,
        },
    ];

    println!("SISTEMA DE ANALISIS DESCRIPTIVO");
    println!();
    mostrar_personas(&personas);
    mostrar_resumen(&personas);
    mostrar_imc(&personas);
    mostrar_retos(&personas);
}
