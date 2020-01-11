// @flow

import React from 'react'
import { Switch } from '@blueprintjs/core'
import { Alignment } from '@blueprintjs/core/lib/cjs/common/alignment'
import './DarkMode.css'
import { getDarkModeEnabled, setDarkModeEnabled } from './darkModeStorage'

const DARK_CLASS = 'bp3-dark'

export default class DarkModeSwitch extends React.Component<{}, { isDark: boolean }> {
  body = document.getElementsByTagName('body')[0]
  state = {
    isDark: this.body.classList.contains(DARK_CLASS)
  }

  componentDidMount () {
    this.setDarkEnabled(getDarkModeEnabled())
  }

  setDarkEnabled = (enabled: boolean) => {
    if (this.state.isDark !== enabled) {
      this.setState({ isDark: enabled })
      setDarkModeEnabled(enabled)
      if (enabled) {
        this.body.classList.add(DARK_CLASS)
      } else {
        this.body.classList.remove(DARK_CLASS)
      }
    }
  }

  render () {
    return <Switch innerLabel='off' innerLabelChecked='on' label='Dark mode' large inline checked={this.state.isDark}
                   alignIndicator={Alignment.RIGHT} style={{ margin: 0 }} onChange={this.onChange}/>
  }

  onChange = (event: SyntheticInputEvent<HTMLInputElement>) => {
    this.setDarkEnabled(event.target?.checked)
  }
}
