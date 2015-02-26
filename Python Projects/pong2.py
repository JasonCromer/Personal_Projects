import pygame

class Player():
	def __init__(self):
		self.x, self.y = 16, SCR_HEI/2
		self.speed = 5
		self.padWid, self.padHei = 8, 64
		self.score = 0
		self.scoreFont = pygame.font.Font("XcelsionItalic.ttf", 40)
	
	def scoring(self):
		scoreBlit = self.scoreFont.render(str(self.score), 1, (255, 255, 255))
		screen.blit(scoreBlit, (32, 16))
		if self.score == 10:
			print("player 1 wins!")
			exit()
	
	def movement(self):
		keys = pygame.key.get_pressed()
		if keys[pygame.K_w]:
			self.y -= self.speed
		elif keys[pygame.K_s]:
			self.y += self.speed
	
		if self.y <= 0:
			self.y = 0
		elif self.y >= SCR_HEI-64:
			self.y = SCR_HEI-64
	
	def draw(self):
		pygame.draw.rect(screen, (255, 255, 255), (self.x, self.y, self.padWid, self.padHei))

class Enemy():
	def __init__(self):
		self.x, self.y = SCR_WID-16, SCR_HEI/2
		self.speed = 3
		self.padWid, self.padHei = 8, 64
		self.score = 0
		self.scoreFont = pygame.font.Font("XcelsionItalic.ttf", 40)
	
	def scoring(self):
		scoreBlit = self.scoreFont.render(str(self.score), 1, (255, 255, 255))
		screen.blit(scoreBlit, (SCR_HEI+92, 16))
		if self.score == 10:
			print("Player 2 wins!")
			exit()
	
	def movement(self):
		keys = pygame.key.get_pressed()
		if keys[pygame.K_UP]:
			self.y -= self.speed
		elif keys[pygame.K_DOWN]:
			self.y += self.speed
	
		if self.y <= 0:
			self.y = 0
		elif self.y >= SCR_HEI-64:
			self.y = SCR_HEI-64
	
	def draw(self):
		pygame.draw.rect(screen, (255, 255, 255), (self.x, self.y, self.padWid, self.padHei))

class Ball():
	def __init__(self):
		self.x, self.y = SCR_WID/3, SCR_HEI/2
		self.speed_x = -3
		self.speed_y = 3
		self.size = 8
	
	def movement(self):
		self.x += self.speed_x
		self.y += self.speed_y

		#wall col
		if self.y <= 0:
			self.speed_y *= -1
		elif self.y >= SCR_HEI-self.size:
			self.speed_y *= -1

		if self.x <= 0:
			self.__init__()
			enemy.score += 1
		elif self.x >= SCR_WID-self.size:
			self.__init__()
			self.speed_x = 3
			player.score += 1
		##wall col
		#paddle col
		#player
		for n in range(-self.size, player.padHei):
			if self.y == player.y + n:
				if self.x <= player.x + player.padWid:
					self.speed_x *= -1
					break
			n += 1
		#enemy
		for n in range(-self.size, enemy.padHei):
			if self.y == enemy.y + n:
				if self.x >= enemy.x - enemy.padWid:
					self.speed_x *= -1
					break
			n += 1
		##paddle col

	def draw(self):
		pygame.draw.rect(screen, (255, 255, 255), (self.x, self.y, 8, 8))

SCR_WID, SCR_HEI = 640, 480
screen = pygame.display.set_mode((SCR_WID, SCR_HEI))
pygame.display.set_caption("Pong")
pygame.font.init()
clock = pygame.time.Clock()
FPS = 60

ball = Ball()
player = Player()
enemy = Enemy()

while True:
	#process
	for event in pygame.event.get():
			if event.type == pygame.QUIT:
				print("Game exited by user")
				exit()
	##process
	#logic
	ball.movement()
	player.movement()
	enemy.movement()
	##logic
	#draw
	screen.fill((0, 0, 0))
	ball.draw()
	player.draw()
	player.scoring()
	enemy.draw()
	enemy.scoring()
	##draw
	#_______
	pygame.display.flip()
	clock.tick(FPS)
