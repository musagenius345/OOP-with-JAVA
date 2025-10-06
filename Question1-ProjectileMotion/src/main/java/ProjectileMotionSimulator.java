import java.util.Scanner;


public class ProjectileMotionSimulator {
    
    // Gravitational acceleration constant (m/s²)
    private static final double GRAVITY = 9.81;
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("===========================================");
        System.out.println("   PROJECTILE MOTION SIMULATOR");
        System.out.println("===========================================\n");
        
        try {
            // Get user input
            System.out.print("Enter initial velocity (m/s): ");
            double velocity = scanner.nextDouble();
            
            System.out.print("Enter launch angle (degrees): ");
            double angleDegrees = scanner.nextDouble();
            
            // Validate input
            if (velocity < 0) {
                System.out.println("\nError: Velocity cannot be negative!");
                return;
            }
            
            if (angleDegrees < 0 || angleDegrees > 90) {
                System.out.println("\nError: Angle must be between 0 and 90 degrees!");
                return;
            }
            
            // Convert angle to radians
            double angleRadians = Math.toRadians(angleDegrees);
            
            // Calculate velocity components
            double velocityX = velocity * Math.cos(angleRadians);
            double velocityY = velocity * Math.sin(angleRadians);
            
            // Calculate time of flight
            // Formula: T = (2 * v₀ * sin(θ)) / g
            double timeOfFlight = (2 * velocityY) / GRAVITY;
            
            // Calculate maximum height
            // Formula: H = (v₀² * sin²(θ)) / (2 * g)
            double maxHeight = (velocityY * velocityY) / (2 * GRAVITY);
            
            // Calculate horizontal range
            // Formula: R = (v₀² * sin(2θ)) / g
            double range = (velocity * velocity * Math.sin(2 * angleRadians)) / GRAVITY;
            
            // Display results
            System.out.println("\n===========================================");
            System.out.println("   SIMULATION RESULTS");
            System.out.println("===========================================");
            System.out.printf("\nInitial Velocity:     %.2f m/s\n", velocity);
            System.out.printf("Launch Angle:         %.2f degrees\n", angleDegrees);
            System.out.printf("Horizontal Velocity:  %.2f m/s\n", velocityX);
            System.out.printf("Vertical Velocity:    %.2f m/s\n", velocityY);
            System.out.println("\n-------------------------------------------");
            System.out.printf("Time of Flight:       %.2f seconds\n", timeOfFlight);
            System.out.printf("Maximum Height:       %.2f meters\n", maxHeight);
            System.out.printf("Horizontal Range:     %.2f meters\n", range);
            System.out.println("===========================================\n");
            
            // Additional trajectory information
            System.out.println("TRAJECTORY INFORMATION:");
            System.out.printf("The projectile will reach maximum height at t = %.2f seconds\n", 
                            timeOfFlight / 2);
            System.out.printf("Optimal angle for maximum range: 45 degrees\n");
            System.out.printf("Your angle efficiency: %.1f%%\n", 
                            (range / ((velocity * velocity) / GRAVITY)) * 100);
            
        } catch (Exception e) {
            System.out.println("\nError: Invalid input! Please enter numeric values.");
        } finally {
            scanner.close();
        }
    }
}