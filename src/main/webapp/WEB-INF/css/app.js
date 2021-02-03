	let buttons = document.querySelectorAll("input");
	if (buttons){
	alert("Ne rabotae");
	}
	if(buttons>=0) alert("namana")
for (let i = 0; i < buttons.length; i++) {
alert("11");
    let button = buttons[i];
    alert("12");
    button.onclick = button_click;
    alert("13");
    button.onmouseleave = button_mouseleave;
    alert("14");
}
async function button_click() {
    let copyText = this.parentElement.parentElement.querySelector("input[type=text]");
alert("2");
    let tooltip = this.firstElementChild;
alert("3");
    // await navigator.clipboard.writeText(copyText.value);
    copyText.hidden = false;
    alert("4");
    copyText.select();
    alert("5");
    document.execCommand("copy");
    alert("6");
    copyText.hidden = true;
    alert("7");

    // let copytext = document.createElement('input')
    // copytext.value = copyText.value
    // document.body.appendChild(copytext)
    // copytext.select()
    // document.execCommand('copy')
    // document.body.removeChild(copytext)
 
    tooltip.innerHTML = "Copied";
}
function button_mouseleave() {
    let tooltip = this.firstElementChild;
    tooltip.innerHTML = "222 to clipboard";
}