// @flow

import type { ComponentType } from 'react'
import React from 'react'
import { Button, H2, Icon, Spinner, Text } from '@blueprintjs/core'
import styled from 'styled-components'

const CenterScreen = styled<{}, {}, 'div'>('div')`
  display: flex;
  flex: 1;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`

function withPromiseResolver<P1: {}, P2: {}> (
  promiseCreator: $Diff<P1, P2> => Promise<P2>
): (ComponentType<P1> => ComponentType<$Diff<P1, P2>>) {
  return (WrappedComponent: ComponentType<P1>) => {
    return class extends React.Component<$Diff<P1, P2>, { error: ?string, props: ?P2 }> {
      state = {}

      componentDidMount () {
        this.startPromise()
      }

      handleClickRefresh = () => {
        this.startPromise()
      }

      startPromise () {
        this.setState({ error: undefined })
        promiseCreator(this.props)
          .then(props => this.setState({ props }))
          .catch(error => {
            this.setState({ error: error.message })
            console.error(error)
          })
      }

      render () {
        if (this.state.error) {
          return <CenterScreen>
            <Icon icon='error' iconSize={36} style={{ padding: '20px' }} />
            <H2>Ein Fehler ist aufgetreten.</H2>
            <Text>{this.state.error}</Text>
            <Button icon='refresh' style={{ margin: '20px' }} text='Erneut versuchen'
                    onClick={this.handleClickRefresh} />
          </CenterScreen>
        } else if (this.state.props) {
          return <WrappedComponent {...this.props} {...this.state.props} />
        } else {
          return <CenterScreen><Spinner /></CenterScreen>
        }
      }
    }
  }
}

export default withPromiseResolver
