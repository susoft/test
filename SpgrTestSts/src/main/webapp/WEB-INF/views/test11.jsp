<%@ page session="false" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<title>HTML 5 문서 구조 잡기</title>

<link href="./resources/js/ex3-9.css" rel="stylesheet" type="text/css">
</head>

<body>
<header> 머릿말 
<nav>
    <ul>
        <li>Nav menu 1</li>
        <li>Nav menu 2</li>
        <li>Nav menu 3</li>
    </ul>

</nav>

</header>
<div id="sidemenu"> 사이드 메뉴 

    <ul>
        <li>Menu 1</li>
        <li>Menu 2</li>
        <li>Menu 3</li>
    </ul>
    <aside>
    	보조적인 asdie 내용
    </aside>
</div>
<article> 
    <hgroup>
        <h1>본문이 들어가는 곳입니다. </h1>
        <h2>훈민정음을 예로 넣었습니다. </h2>
    </hgroup>

    <figure> <img src="./resources/images/2010.04.29.raw.124.jpg" width="241" height="160">
    <figcaption> 그림 설명 들어가는 곳</figcaption>
    </figure>
    <p>나랏님(세종) 말씀이, 우리나라의 말이 중국말과 달라 <mark>한자로는 서로 (의사)소통하지 아니하므로</mark> 이런 까닭에 
       어리석은 백성이 이르고자 하는 바가 있어도 마침내 그 뜻을 (글자에) 실어서 펴지 못하는 사람이 많으니라. 내가 이를 불쌍히 
        여겨서 새로 스물여덟개의 글자를 만드니 사람마다 쉽게 익혀서 날로 써서 편안하게 할 따름이니라.</p>         
    <section> 섹션이 들어가는 곳 </section>
</article>

<footer> 꼬릿말 </footer>

</body>
</html>
