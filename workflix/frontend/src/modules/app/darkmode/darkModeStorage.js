// @flow

const STORAGE_KEY = 'dark mode'

export function setDarkModeEnabled (enabled: boolean) {
  if (enabled) {
    localStorage.setItem(STORAGE_KEY, 'enabled')
  } else {
    localStorage.removeItem(STORAGE_KEY)
  }
}

export function getDarkModeEnabled (): boolean {
  return Boolean(localStorage.getItem(STORAGE_KEY))
}
