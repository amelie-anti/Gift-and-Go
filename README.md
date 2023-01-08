ANTIGNY Amélie 
M2 MAIGE 2022-2023
Projet Programation mobile

# GIFT AND GO

Bienvenue dans l'application Gift And Go !!!

## RESUME : 

Avec cette application, vous pouvez créer et lancer des chasses aux trésors sur une carte. Vous pouvez positionner des points sur la carte et lorsque vous arrivez sur l'un d'entre eux, la caméra de votre téléphone se lance et affiche un animal avec un cadeau à la main. Une fois que vous avez capturé l'animal, le marqueur passe du rouge au vert.

Vous pouvez également revoir les mignons animaux que vous avez capturés dans l'album de l'application.

Cette application a été réalisée en Java avec Android Studio. Les images sont générées grâce à DALL-E, il faut donc entrer une clé API avant de lancer l'application. Pour utiliser l'API DALL-E, vous devez vous rendre sur le [site de OpenAI](https://beta.openai.com/docs/quickstart) et suivre les instructions pour obtenir une clé API puis ensuite la renseigner ligne 35 de la classe PostTask "Bearer sk-xxxxxxxxx".

J'espère que vous apprécierez cette application de chasses aux trésors ! N'hésitez pas à nous faire part de vos commentaires et suggestions.

## PRÉREQUIS  : 
  - Android Studio
  - Clé API OPEN IA pour DALL-E
  - J'ai développer l'application sur mon téléphone un Huawei P30
 
## UTILISATION :
Cette application peut avoir plusieurs but d'utilisation, par exemple pour faire une activité extérieure avec des enfants : 
Le but est de trouver des « cadeaux » dans des lieux précis.  
On se met dans le parc de l’université et la personne va devoir « attraper des indices » à chaque « indice » qu'il va revenir sur la personne qui organisait la chasse au trésor pour lui montrer son album avec les preuves qu'il s'est bien rendu à l'endroit indiqué. Et elle va lui donner soit un indice qu’il va devoir résoudre soit un petit cadeau (de type un bonbon). Puis il retourne pour chercher d’autres indices afin d'avoir d’autre petit cadeaux. 

Sinon à la fin du jeu un cadeau peut être donnée, une fois que tout les animaux mignons et leur cadau ont été attrapé. 

Cette application peut être utiliser pour Halloween quand l’on n’a pas de voisinage ou quand on n’a pas envie de prendre des bonbons d’inconnus. 

C’est sympa de faire une petite chasse au trésor afin d'offrir un cadeau au lieu de simplement le donner.
Cela permet aussi de faire une petite balade en même temps avec la famille pendant que l’enfant cherche les animaux pour avoir son cadeau et un objectif de balade. 

## COMMENT JOUER :

Crée une chasse au trésor :
            1- Cliquer sur Crée une chasse au trésor;
            2- Puis cliquait sur Ajouter;
            3- Renseigner un nom de chasse;
            4- Cliquer sur la carte pour ajouter des points (cela peut prendre un certain temps)
            5- Une fois tous les points ajoutés vous pouvez retourner sur la page d'accueil.
Modifier une chasse au trésor :
            1- Cliquer sur Crée une chasse au trésor;
            2- Cliquer sur le nom de la chasse à modifier;
            3- Pour ajouter un point cliquer sur la carte à l'endroit désiré;
            4- Pour supprimer un point cliquer sur le point a supprimé.
Lancer une partie :
            1- Il faut préalablement avoir créé une chasse au trésors,
            2- Cliquer sur la chasse sur lequel vous voulez jouer,
            3- Déplacer vous à l'endroit signaler sur la carte pour trouver votre cadeaux,
            4- Une fois attraper le point devient vert.
            
            
            

## DEMO VIDEO DE GIFT AND GO : 
https://user-images.githubusercontent.com/58032469/211219264-94f2e797-92a3-4cfa-9d41-ddfd272e82d9.mp4


## DESCRIPTION DES CLASSES DE L'APPLICATION :

- "Welcome" est la classe qui va nous accueilir sur l'application, grace à cette classe nous avons une simulation d'animation d'ouverture d'application, nous pouvons y trouver le logo de l'application ainsi que mon nom. La classe utilise activity_opening.xml.
- "MainActivity" est la classe qui va représenter la page principale, nous y demandons les différentes autorisations. La classe utilise activity_main.xml. Nous retrouvons sur cette  







