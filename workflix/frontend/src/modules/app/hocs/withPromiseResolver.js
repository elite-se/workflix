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
  height: 100%;
`

function withPromiseResolver<P1: {}, P2: {}> (
  promiseCreator: ($Diff<P1, P2>, (soft: boolean) => void) => Promise<P2>,
  shouldUpdate: (oldProps: $Diff<P1, P2>, newProps: $Diff<P1, P2>) => boolean = () => false
): (ComponentType<P1> => ComponentType<$Diff<P1, P2>>) {
  return (WrappedComponent: ComponentType<P1>) => {
    return class extends React.Component<$Diff<P1, P2>, { error: ?string, props: ?P2, softLoading: boolean }> {
      state = { softLoading: false, error: undefined, props: undefined }

      componentDidMount () {
        this.startPromise(false)
      }

      componentDidUpdate (prevProps: $Diff<P1, P2>) {
        if (shouldUpdate(prevProps, this.props)) {
          this.startPromise(false)
        }
      }

      handleClickRefresh = () => {
        this.startPromise(false)
      }

      refresh = (soft: boolean) => {
        this.startPromise(soft)
      }

      startPromise (soft: boolean) {
        this.setState(state => ({ error: undefined, props: soft ? state.props : undefined, softLoading: soft }))
        promiseCreator(this.props, this.refresh)
          .then(props => this.setState({ props, softLoading: false }))
          .catch(error => {
            this.setState({ error: error.message, softLoading: false })
            console.error(error)
          })
      }

      render () {
        const { error, props, softLoading } = this.state
        if (error) {
          return <CenterScreen>
            <Icon icon='error' iconSize={36} style={{ padding: '20px' }}/>
            <H2>Ein Fehler ist aufgetreten.</H2>
            <Text>{error}</Text>
            <Button icon='refresh' style={{ margin: '20px' }} text='Erneut versuchen'
                    onClick={this.handleClickRefresh}/>
          </CenterScreen>
        } else if (props && !softLoading) {
          return <WrappedComponent {...this.props} {...props}/>
        } else if (props && softLoading) {
          return <>
            <WrappedComponent {...this.props} {...props}/>
            <CenterScreen style={{ position: 'absolute', top: '0', right: '0', left: '0', bottom: '0' }}>
              <Spinner/>
            </CenterScreen>
          </>
        } else {
          return <CenterScreen>
            <Spinner/>
          </CenterScreen>
        }
      }
    }
  }
}

export default withPromiseResolver
