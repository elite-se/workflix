// @flow

// pretty hacky: "remove" the class of the Blueprint.js overlay between portal and drawer that prevents clicks in
// the rest of the page
const onOpenRemoveOverlayClass = (elem: HTMLElement) => {
  const overlayElem = elem.parentElement
  if (overlayElem == null) {
    return
  }
  overlayElem.classList.remove('bp3-overlay-container')
}

export default onOpenRemoveOverlayClass
