import pygame
import random

# Initializion
pygame.init()

# Display setup
width, height = 400, 600
screen = pygame.display.set_mode((width, height))
pygame.display.set_caption("Flappy Bird")

# Colors
white = (255, 255, 255)
blue = (0, 0, 255)
green = (0, 255, 0)
yellow = (255, 255, 0)
black = (0, 0, 0)

# Bird parameters
bird_radius = 20
bird_x = 50
bird_y = height // 2
bird_vel = 0
gravity = 0.5

# Pipe parameters
gap = 170

pipe_width = 70
pipe_height = random.randint(150, 450)
pipe_x = width
pipe_y = 0
pipe_vel = 3

pipe2_width = 70
pipe2_height = 600 - pipe_height + gap
pipe2_x = width
pipe2_y = pipe_height + gap
pipe2_vel = 3

# Score parameters
score = 0
font = pygame.font.Font(None, 36)

# Functions to display objects
def display_score():
    score_surface = font.render(f"Score: {score}", True, white)
    screen.blit(score_surface, (10, 10))

def display_bird():
    pygame.draw.circle(screen, yellow, (bird_x, int(bird_y)), bird_radius)

def display_pipes():
    pygame.draw.rect(screen, green, (pipe_x, pipe_y, pipe_width, pipe_height))
    pygame.draw.rect(screen, green, (pipe_x - 5, pipe_height - 20, pipe_width + 10, 40))

    pygame.draw.rect(screen, green, (pipe2_x, pipe2_y, pipe2_width, pipe2_height))
    pygame.draw.rect(screen, green, (pipe2_x - 5, pipe2_y - 20, pipe_width + 10, 40))

# Function that detects collisions
def check_collision():
    if bird_y > height or bird_y < 0:
        return True
    if bird_x + bird_radius > pipe_x and bird_x - bird_radius < pipe_x + pipe_width:
        if  bird_y - bird_radius < pipe_height + 18 or bird_y + bird_radius > pipe2_y - 18:
            return True
    return False

# Runs the game
clock = pygame.time.Clock()
running = True
while running:
    screen.fill(blue)

    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False
        if event.type == pygame.KEYDOWN: #bird jumps when the spacebar is pressed
            if event.key == pygame.K_SPACE:
                bird_vel = -8

    bird_y += bird_vel
    bird_vel += gravity

    pipe_x -= pipe_vel
    pipe2_x -= pipe_vel

    if pipe_x < -pipe_width: #score increments for every pipe passed
        pipe_x = width
        pipe2_x = width
        pipe_height = random.randint(100, 450)
        pipe2_y = pipe_height + gap
        pipe2_height = 600 - pipe_height + gap
        score += 1

    if check_collision(): #game keeps running until there is a collision
        running = False

    display_pipes()
    display_bird()
    display_score()

    pygame.display.flip()
    clock.tick(60)

pygame.quit()