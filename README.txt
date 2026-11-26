# The Blind Executioner

Mod para Minecraft Java 1.16.5 (Forge). Un Verdugo ciego te castiga por tus pecados, pero solo si un testigo (un cuervo) te vio cometerlos.

## Como funciona
- **Pecados:** sangre, codicia y cobardia. Cada acto suma puntos segun su gravedad.
- **Testigo:** si un cuervo te ve pecar, vuela 5 segundos para delatarte. Si lo matas a tiempo, el crimen queda impune.
- **Juicio:** un arbol de decision elige la sentencia segun tu pecado dominante y su nivel (1 a 3).
- **Verdugo:** es ciego y te busca por el ruido. Agachado no te oye.

## Estado
- [x] Contadores de pecados y comando `/juicio info`
- [x] Arbol de decision
- [x] Cuervo Testigo (provisional, usa un loro)
- [x] Verdugo provisional con sistema de ruido
- [x] Cobardia
- [ ] Una prueba distinta por sentencia
- [ ] Pluma Palida y Semilla Dorada
- [ ] Entidades y modelos propios

## Requisitos
JDK 8 y Minecraft Forge 1.16.5 (MDK 36.2.34)

## Ejecutar
    ./gradlew genEclipseRuns
    ./gradlew runClient

## Comandos de prueba
- `/juicio info`, `/juicio sentencia`, `/juicio reiniciar`
- `/cuervo` invoca un testigo
- `/verdugo` invoca al Verdugo
