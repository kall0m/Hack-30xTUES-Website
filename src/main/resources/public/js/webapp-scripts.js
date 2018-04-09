$(function() {
    setTimeout(function() {
        $('#message').fadeOut();
    }, 5000);
});

$(function() {
    $("#participants_count").on("change",function() {
        var e = document.getElementById("participants_count");
        var participantsCount = e.options[e.selectedIndex].value;

        var participant3 = document.getElementById("participant3");
        var participant4 = document.getElementById("participant4");
        var participant5 = document.getElementById("participant5");

        if(participantsCount === "3") {
            participant4.style.display = 'none';
            document.getElementById("user_email4").removeAttribute("required");
            participant5.style.display = 'none';
            document.getElementById("user_email5").removeAttribute("required");
            document.getElementById("want_teammates_div").style.display = 'block';
        } else if(participantsCount === "4") {
            participant4.style.display = 'block';
            document.getElementById("user_email4").setAttribute("required", "required");
            participant5.style.display = 'none';
            document.getElementById("user_email5").removeAttribute("required");
            document.getElementById("want_teammates_div").style.display = 'block';
        } else if(participantsCount === "5") {
            participant4.style.display = 'block';
            document.getElementById("user_email4").setAttribute("required", "required");
            participant5.style.display = 'block';
            document.getElementById("user_email5").setAttribute("required", "required");
            document.getElementById("want_teammates_div").style.display = 'none';
        }
    });
});

// Form with Multiple Steps

var currentTab = 0; // Current tab is set to be the first tab (0)
showTab(currentTab); // Display the crurrent tab

function showTab(n) {
    // This function will display the specified tab of the form...
    var x = document.getElementsByClassName("tab");
    x[n].style.display = "block";
    //... and fix the Previous/Next buttons:
    if (n == 0) {
        document.getElementById("prevBtn").style.display = "none";
    } else {
        document.getElementById("prevBtn").style.display = "inline";
    }
    if (n == (x.length - 1)) {
        document.getElementById("nextBtn").innerHTML = "Регистрация";
        document.getElementById("nextBtnSettings").innerHTML = "Запази";
    } else {
        document.getElementById("nextBtn").innerHTML = "Продължи";
        document.getElementById("nextBtnSettings").innerHTML = "Продължи";
    }
    //... and run a function that will display the correct step indicator:
    fixStepIndicator(n)
}

function nextPrev(n) {
    // This function will figure out which tab to display
    var x = document.getElementsByClassName("tab");
    // Exit the function if any field in the current tab is invalid:
    if (n == 1 && !validateForm()) return false;
    // Hide the current tab:
    x[currentTab].style.display = "none";
    // Increase or decrease the current tab by 1:
    currentTab = currentTab + n;
    // if you have reached the end of the form...
    if (currentTab >= x.length) {
        // ... the form gets submitted:
        if(checker()) {
            document.getElementById("regForm").submit();
        }

        return false;
    }
    // Otherwise, display the correct tab:
    showTab(currentTab);
}

function validateForm() {
    // This function deals with validation of the form fields
    var x, y, i, valid = true;
    x = document.getElementsByClassName("tab");
    y = x[currentTab].getElementsByTagName("input");
    // A loop that checks every input field in the current tab:
    for (i = 0; i < y.length; i++) {
        // If a field is empty...
        if(((y[i] != document.getElementById("user_email4") || document.getElementById("user_email4").hasAttribute("required")) &&
            ((y[i] != document.getElementById("user_email5")) || document.getElementById("user_email5").hasAttribute("required"))) &&
            (y[i] != document.getElementById("companyInput")) && (y[i] != document.getElementsByClassName("technologiesCheckbox"))) {
            if (y[i].value == "") {
                // add an "invalid" class to the field:
                y[i].className += " invalid";
                // and set the current valid status to false
                valid = false;
            }
        }
    }

    // If the valid status is true, mark the step as finished and valid:
    if (valid) {
        document.getElementsByClassName("step")[currentTab].className += " finish";
    }
    return valid; // return the valid status
}

function fixStepIndicator(n) {
    // This function removes the "active" class of all steps...
    var i, x = document.getElementsByClassName("step");
    for (i = 0; i < x.length; i++) {
        x[i].className = x[i].className.replace(" active", "");
    }
    //... and adds the "active" class on the current step:
    x[n].className += " active";
}

//Vladi's function for checking input
function checker(){
    let values = [];

    $('input').each(function(){
        values.push($(this).val());
    });

    $('textarea').each(function(){
        values.push($(this).val());
    });

    let sqlSymbols = ["--", ";", "`", "'", "DROP DATABASE", "DROP TABLE"];

    let flag = true;

    values.map(function (x){
        sqlSymbols.map(function(y){
            if(x.indexOf(y) > -1){
                flag =  false;
            }
        })
    });

    if(!flag) {
        $('form').append("<div id=\"message\" class=\"notification pos-right pos-top col-sm-6 col-md-4 col-xs-12 growl-notice notification--reveal\" data-animation=\"from-right\" data-notification-link=\"growl\" style=\"top: 40px;\"><div class=\"boxed boxed--sm bg--dark\"><span>Невалидни символи!</span></div><div class=\"notification-close-cross notification-close\"></div></div>");
    }

    return flag;
}

//Replace URLs in text with links
function replaceURLWithHTMLLinks(text) {
    var re = /(\(.*?)?\b((?:https?|ftp|file):\/\/[-a-z0-9+&@#\/%?=~_()|!:,.;]*[-a-z0-9+&@#\/%=~_()|])/ig;
    return text.replace(re, function(match, lParens, url) {
        var rParens = '';
        lParens = lParens || '';

        // Try to strip the same number of right parens from url
        // as there are left parens.  Here, lParenCounter must be
        // a RegExp object.  You cannot use a literal
        //     while (/\(/g.exec(lParens)) { ... }
        // because an object is needed to store the lastIndex state.
        var lParenCounter = /\(/g;
        while (lParenCounter.exec(lParens)) {
            var m;
            // We want m[1] to be greedy, unless a period precedes the
            // right parenthesis.  These tests cannot be simplified as
            //     /(.*)(\.?\).*)/.exec(url)
            // because if (.*) is greedy then \.? never gets a chance.
            if (m = /(.*)(\.\).*)/.exec(url) ||
                    /(.*)(\).*)/.exec(url)) {
                url = m[1];
                rParens = m[2] + rParens;
            }
        }
        return lParens + "<a href='" + url + "'>" + url + "</a>" + rParens;
    });
}

//Search mentors by technologies
function searchMentorsByTechnologies() {
    var input, filter, ul, li, tech, i;
    input = document.getElementById("searchByTechnologies");
    filter = input.value.toUpperCase();
    ul = document.getElementById("listOfMentors");
    li = ul.getElementsByTagName("li");
    for (i = 0; i < li.length; i++) {
        tech = li[i].getElementsByTagName("p")[0];
        if (tech.innerHTML.toUpperCase().includes(filter)) {
            li[i].style.display = "";
        } else {
            li[i].style.display = "none";
        }
    }
}

//When searching not to show the taken mentors
/*function myFunction() {
 var input, filter, ul, li, tech, i, teamCount;
 input = document.getElementById("myInput");
 filter = input.value.toUpperCase();
 ul = document.getElementById("myUL");
 li = ul.getElementsByTagName("li");
 for (i = 0; i < li.length; i++) {
 tech = li[i].getElementsByTagName("p")[0];
 teamCount = li[i].getElementsByClassName("teamCount")[0];
 if(filter === "") {
 li[i].style.display = "";
 } else if (tech.innerHTML.toUpperCase().includes(filter) && teamCount.innerHTML !== "Оставащи отбори: 0") {
 li[i].style.display = "";
 } else {
 li[i].style.display = "none";
 }
 }
 }*/