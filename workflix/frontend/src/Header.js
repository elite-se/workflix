// @flow

import React from 'react'
import { Button, Navbar } from '@blueprintjs/core'
import { Alignment } from '@blueprintjs/core/lib/cjs/common/alignment'
import { Link } from '@reach/router'

const QUARTER_HOUR = 900000
const THOUSAND = 1000

class Header extends React.Component<{}, { title: React$Node }> {
  state = { title: 'Workflix' }

  componentDidMount () {
    setTimeout(this.wontfix, QUARTER_HOUR)
  }

  wontfix = () => {
    this.setState(state => ({
      title: state.title === 'Workflix' ? <span style={{ fontSize: '1.04em' }}>Won'tfix</span> : 'Workflix'
    }))
    setTimeout(this.wontfix, THOUSAND * Math.random())
  }

  render () {
    return <Navbar>
      <Navbar.Group align={Alignment.LEFT}>
        <Navbar.Heading>{this.state.title}</Navbar.Heading>
        <Navbar.Divider onClick={this.wontfix}/>
        <Link to='/'><Button className='bp3-minimal' icon='layers' text='Tasks overview'/></Link>
        <Link to='process-templates'><Button className='bp3-minimal' icon='new-layers' text='Process templates'/></Link>
        <Link to='users'><Button className='bp3-minimal' icon='user' text='Users'/></Link>
      </Navbar.Group>
    </Navbar>
  }
}

export default Header
