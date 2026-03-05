# Stronghold Finder

Stronghold Finder is a small Java utility that reproduces the **stronghold placement algorithm used in early Minecraft Beta versions (Beta 1.9 pre releases)**. Given a world seed, the program calculates the approximate locations of the three strongholds that Minecraft would generate using the original radial placement logic. The output includes both **block coordinates and chunk coordinates** for each predicted stronghold. Because the program does not perform Minecraft’s biome validation step, the real in-game stronghold may be slightly offset from the predicted position.

## Usage

Compile the program:

```
javac java/mi/m4x/strongholdfinder/StrongholdFinder.java
```

Run it with a world seed:

```
java StrongholdFinder <seed>
```

Example:

```
java Strongholdfinder  123456789
```

If no seed is provided, the program will use the default seed defined in the source code.

## License

This project is released under the **MIT License**. You are free to use, modify, and distribute it with proper attribution.


