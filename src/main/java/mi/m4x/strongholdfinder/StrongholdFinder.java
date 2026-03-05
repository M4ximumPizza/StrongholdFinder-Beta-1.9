package mi.m4x.strongholdfinder;

import java.util.Random;

/**
 * StrongholdFinder
 *
 * A lightweight utility that reproduces the core stronghold placement
 * algorithm used in early Minecraft Beta versions (specifically Beta 1.9 Pre-3).
 *
 * This program calculates the theoretical locations of the three strongholds
 * generated in a world using only the world seed. It reproduces the radial
 * placement logic used by Minecraft's MapGenStronghold class but omits biome
 * correction and world lookups.
 *
 * ---------------------------------------------------------------------------
 * Background
 * ---------------------------------------------------------------------------
 *
 * In early Minecraft versions, strongholds were generated using a deterministic
 * algorithm based solely on the world seed. The game would:
 *
 * 1. Seed a Random instance with the world seed.
 * 2. Generate a random starting angle around the origin.
 * 3. Place 3 strongholds in a circular ring around spawn.
 * 4. Each stronghold is spaced evenly around the circle (120° apart).
 * 5. Each stronghold's radius from the origin is randomized slightly.
 *
 * The original Minecraft code performing this logic exists inside
 * net.minecraft.src.MapGenStronghold.
 *
 * This tool replicates the following portion of that algorithm:
 *
 *     rand.setSeed(worldSeed);
 *     angle = rand.nextDouble() * 2π;
 *
 *     for each stronghold:
 *         distance = (1.25 + rand.nextDouble()) * 32
 *         chunkX = round(cos(angle) * distance)
 *         chunkZ = round(sin(angle) * distance)
 *         angle += 2π / numberOfStrongholds
 *
 * The result is the chunk coordinates where strongholds will generate.
 *
 * ---------------------------------------------------------------------------
 * Important Differences From Minecraft
 * ---------------------------------------------------------------------------
 *
 * The real Minecraft implementation performs additional steps that are NOT
 * reproduced here:
 *
 * 1. Biome validation
 *    Minecraft checks that strongholds generate only in specific biomes
 *    (desert, forest, hills, swampland).
 *
 * 2. Biome searching
 *    If the calculated position is not valid, Minecraft searches up to
 *    112 blocks away for a valid biome.
 *
 * 3. Structure generation
 *    Minecraft later generates the actual stronghold structure at the
 *    selected chunk coordinates.
 *
 * This tool ignores those steps and instead prints the raw calculated
 * coordinates.
 *
 * As a result, coordinates from this program may differ slightly from the
 * final in-game stronghold location if biome correction occurs.
 *
 * ---------------------------------------------------------------------------
 * Output Format
 * ---------------------------------------------------------------------------
 *
 * For each stronghold the program prints:
 *
 * Stronghold N:
 *      Block Coordinates (X, Z)
 *      Chunk Coordinates (chunkX, chunkZ)
 *
 * Block coordinates are calculated as:
 *
 *      blockX = (chunkX << 4) + 8
 *      blockZ = (chunkZ << 4) + 8
 *
 * This matches Minecraft's logic of placing structures near the center
 * of the chunk.
 *
 * ---------------------------------------------------------------------------
 * Usage
 * ---------------------------------------------------------------------------
 *
 * Run with an optional world seed:
 *
 *      java StrongholdFinder <seed>
 *
 * Example:
 *
 *      java StrongholdFinder 1234567890
 *
 * If no seed is provided, a default seed is used.
 *
 * ---------------------------------------------------------------------------
 *
 * @author M4ximumpizza
 */

public class StrongholdFinder {

    /**
     * Entry point for the stronghold finder.
     *
     * Accepts an optional command-line argument specifying the world seed.
     * If no seed is provided, a predefined default seed is used.
     *
     * @param args optional argument containing the world seed
     */
    public static void main(String[] args) {

        // Default world seed
        long seed = 12345L;

        // Allow the seed to be overridden via command line
        if (args.length > 0) {
            try {
                seed = Long.parseLong(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid seed, using default.");
            }
        }

        findStrongholds(seed);
    }

    /**
     * Calculates the positions of the three strongholds generated in
     * early Minecraft Beta versions.
     *
     * The algorithm replicates the radial placement logic used by
     * MapGenStronghold in Beta 1.9 Pre-3.
     *
     * Steps:
     * 1. Seed a Random object using the world seed.
     * 2. Generate a random starting angle.
     * 3. Place three strongholds evenly spaced around a circle.
     * 4. Randomize each stronghold's distance slightly.
     *
     * The computed coordinates represent the chunk location where the
     * stronghold structure will attempt to generate.
     *
     * @param seed the Minecraft world seed
     */
    public static void findStrongholds(long seed) {

        Random rand = new Random();
        rand.setSeed(seed);

        // Initial angle (var3 in the original Minecraft code)
        double angle = rand.nextDouble() * Math.PI * 2.0D;

        // Early Minecraft generated exactly 3 strongholds
        int numStrongholds = 3;

        System.out.println("Finding Strongholds for Seed: " + seed);
        System.out.println("--------------------------------------");

        for (int i = 0; i < numStrongholds; ++i) {

            /*
             * Radius calculation.
             *
             * (1.25 + random) * 32
             *
             * This produces a ring roughly 40–72 chunks from the origin.
             */
            double distance = (1.25D + rand.nextDouble()) * 32.0D;

            // Convert polar coordinates to chunk coordinates
            int chunkX = (int)Math.round(Math.cos(angle) * distance);
            int chunkZ = (int)Math.round(Math.sin(angle) * distance);

            /*
             * Convert chunk coordinates to block coordinates.
             *
             * (chunk << 4) converts chunk -> block
             * +8 centers the structure in the chunk
             */
            int blockX = (chunkX << 4) + 8;
            int blockZ = (chunkZ << 4) + 8;

            System.out.printf(
                    "Stronghold %d: X = %d, Z = %d | (Chunk: %d, %d)\n",
                    (i + 1), blockX, blockZ, chunkX, chunkZ
            );

            // Move to the next 120° position on the ring
            angle += Math.PI * 2.0D / (double)numStrongholds;
        }
    }
}