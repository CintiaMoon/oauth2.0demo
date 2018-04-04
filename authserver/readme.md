授权服务
----

[OAuth 2.0 授权框架](https://tools.ietf.org/html/draft-ietf-oauth-v2-31)
---

一分钟了解 OAuth2.0

4种角色：

- 资源所有者：可以授权给受保护资源的实体。如果资源所有者是人，那么它就是终端用户。
- 资源服务器：用于托管受保护资源的服务器。可以通过访问令牌来接受处理对受保护资源的请求，并作出响应。
- 客户端：一个使用授权来请求受保护资源的应用。客户端不需要特定实现，它可以是web服务、桌面程序、或其他设备。
- 授权服务：一旦成功从资源所有者授权获授权，服务器就会向客户端发送访问令牌。

协议流

     +--------+                               +---------------+
     |        |--(A)- Authorization Request ->|   Resource    |
     |        |                               |     Owner     |
     |        |<-(B)-- Authorization Grant ---|               |
     |        |                               +---------------+
     |        |
     |        |                               +---------------+
     |        |--(C)-- Authorization Grant -->| Authorization |
     | Client |                               |     Server    |
     |        |<-(D)----- Access Token -------|               |
     |        |                               +---------------+
     |        |
     |        |                               +---------------+
     |        |--(E)----- Access Token ------>|    Resource   |
     |        |                               |     Server    |
     |        |<-(F)--- Protected Resource ---|               |
     +--------+                               +---------------+

                     Figure 1: Abstract Protocol Flow


- (A) 客户端向资源所有者请求授权。（可以像图上画的这样直接请求，或者更好的办法是间接地以授权服务器作为中介）
- (B) 客户端收到授权许可。（授权许可是一个代表了资源所有者的授权的凭证，授权许可使用规范定义的4种类型或扩展的许可类型。
      授权许可类型由客户端使用的请求授权方法和授权服务器支持的许可类型决定。）
- (C) 客户端通过出示授权许可从授权服务器以获取访问令牌。
- (D) 授权服务器鉴定客户端，验证授权许可，确定访问令牌是否有效。
- (E) 客户端通过出示访问令牌给资源服务器来访问受保护资源。
- (F) 资源服务器验证访问令牌，如果合法则服务请求。

> 注意：通常是以授权服务作为中介，从资源所有者那里获取授权给客户端（AB步骤）

刷新超时的 token


      +--------+                                           +---------------+
      |        |--(A)------- Authorization Grant --------->|               |
      |        |                                           |               |
      |        |<-(B)----------- Access Token -------------|               |
      |        |               & Refresh Token             |               |
      |        |                                           |               |
      |        |                            +----------+   |               |
      |        |--(C)---- Access Token ---->|          |   |               |
      |        |                            |          |   |               |
      |        |<-(D)- Protected Resource --| Resource |   | Authorization |
      | Client |                            |  Server  |   |     Server    |
      |        |--(E)---- Access Token ---->|          |   |               |
      |        |                            |          |   |               |
      |        |<-(F)- Invalid Token Error -|          |   |               |
      |        |                            +----------+   |               |
      |        |                                           |               |
      |        |--(G)----------- Refresh Token ----------->|               |
      |        |                                           |               |
      |        |<-(H)----------- Access Token -------------|               |
      +--------+           & Optional Refresh Token        +---------------+

                   Figure 2: Refreshing an Expired Access Token

授权码许可

authorization code 许可类型用于获取 access token 和 refresh token，
为保密的客户端做了优化。
作为一个基于重定向的协议流，
客户端必须能跟资源所有者的用户代理（user-agent 通常是 web 浏览器）交互，
能够接受从授权服务重定向过来的请求。

     +----------+
     | Resource |
     |   Owner  |
     |          |
     +----------+
          ^
          |
         (B)
     +----|-----+          Client Identifier      +---------------+
     |         -+----(A)-- & Redirection URI ---->|               |
     |  User-   |                                 | Authorization |
     |  Agent  -+----(B)-- User authenticates --->|     Server    |
     |          |                                 |               |
     |         -+----(C)-- Authorization Code ---<|               |
     +-|----|---+                                 +---------------+
       |    |                                         ^      v
      (A)  (C)                                        |      |
       |    |                                         |      |
       ^    v                                         |      |
     +---------+                                      |      |
     |         |>---(D)-- Authorization Code ---------'      |
     |  Client |          & Redirection URI                  |
     |         |                                             |
     |         |<---(E)----- Access Token -------------------'
     +---------+       (w/ Optional Refresh Token)

    Note: The lines illustrating steps A, B, and C are broken into two
    parts as they pass through the user-agent.

                     Figure 3: Authorization Code Flow

- A 客户端定向资源所有者代理到 authorization endpoint。
    一旦访问被许可，
    客户端列举它的id、请求范围、位置状态以及一个用于返回的重定向uri。
- B 授权服务鉴定资源所有者，确定资源所有者是否许可了客户端的访问请求。
- C 假设资源所有者许可了访问，授权服务使用前面提供的uri重定向用户代理回到客户端。
    重定向uri携带了客户端前面提供的一个 authorization code、和任意的位置状态。
- D 客户端通过出示从前几步获取的 authorization code，从授权服务器的 token endpoint 请求 access token。
    在做这个请求时，客户端被授权服务认证。
    客户端继续列举用户获取重 authorization code 的定向uri。
- E 授权服务授权给客户端，验证 authorization code，保证收到的重定向 uri 匹配“步骤 C”中用于重定向客户端的uri。
     如果验证通过，授权服务器返回响应：一个 access token，和可选的 refresh token。

测试本项目是否正常
---------

#### 测试单点登录的客户端授权服务：

- 使用 client_credentials 许可类型，获取 access token（图1步骤ABCD，客户端直接授权，通常仅供测用）
```
curl ssoclient:secret@localhost:9000/oauth/token -d grant_type=client_credentials
```

- 使用 password 许可类型，出示用户名、密码获取 access token（图1步骤ABCD）
```
curl ssoclient:secret@localhost:9000/oauth/token -d grant_type=password -d username=cici -d password=123456
```

- 出示 access token， 访问资源服务（图1步骤EF）
```
curl --header "Authorization: Bearer e5210c79-31eb-4c9d-97f5-b44f6528cc04" http://localhost:9000/me
```

#### 测试 API 授权服务

- 使用 password 许可类型，获取 access token 和 refresh token （图2 步骤AB）
```
curl apiclient:secret@localhost:9000/oauth/token -d grant_type=password -d username=cici -d password=123456
```

- 出示 access token 访问资源服务，直至 access token 失效（图2 步骤CDEF）
```
curl --header "Authorization: Bearer 8fb4e4a3-8b3c-4a44-9b0e-8e44680ca24b" http://localhost:9000/me
```

- 出示 refresh token 换取 access token （图2 步骤GH）
```
curl apiclient:secret@localhost:9000/oauth/token -d grant_type=refresh_token -d refresh_token=ac5e9509-50a4-4be6-a56e-52d8c69d8f03
```

#### 在项目中，单点登录使用的是授权码许可(authorization code)

- 在客户端应用使用第三方授权，被重定向到授权页面，输入凭证，比如用户名、密码，验证通过后，获得授权码（图3 步骤ABC）
```
http://localhost:9000/oauth/authorize?client_id=ssoclient&redirect_uri=http://localhost:8090/client/login&response_type=code&state=UeMXVu
```

- 客户端出示授权码，从授权服务换取 access token 并默认重定向回到客户端，（示例代码从授权服务的 user-info-uri 获取到了用户信息）（图3 步骤DE）
```
http://localhost:9000/oauth/authorize?client_id=ssoclient&redirect_uri=http://localhost:8090/client/login&response_type=code&state=UeMXVu
http://localhost:8090/client/login?code=H1HU1X&state=UeMXVu
```

[Spring OAuth 2.0 开发指南](https://github.com/spring-projects/spring-security-oauth/blob/master/docs/oauth2.md)
---------------------

本用户指南分为两个部分，
第一部分是OAuth2.0提供端（OAuth 2.0 Provider），
第二部分是OAuth2.0的客户端（OAuth 2.0 Client）

OAuth 2.0 提供端
-------------

OAuth 2.0 的提供端的用途是负责将受保护的资源暴露出去。
配置包括建立一个可以访问受保护的资源的客户端代表。
提供端是通过管理和验证可用于访问受保护的资源的 OAuth 2.0 令牌来做的。
在适当的地方，提供端也必须为用户提供一个用于确认客户端 是否能够访问受保护的资源的接口（也就是一个页面或是一个窗口）。

OAuth 2.0 提供端实现
---------------

在 OAuth 2 提供者其实是分成“授权服务”和“资源服务”两个角色的，
而这两个角色有时是存在于同一个应用程序中的，
通过 Spring Security OAuth 你可以有选择的将它们分裂到两个应用程序中，
也可以有选择的给授权服务配置多个资源服务。
获取令牌（Tokens）请求是由Spring MVC的控制器端点来处理的，
访问受保护的资源是通过标准的Spring Security请求过滤器来处理的。

实现 Spring Security 过滤器链实现 OAuth 2.0 授权服务必须用到的端点：
- AuthorizationEndpoint 用于服务请求授权: /oauth/authorize.
- TokenEndpoint 用于服务请求访问令牌: /oauth/token.

实现 OAuth 2.0 授权服务必须要用到的过滤器：
- OAuth2AuthenticationProcessingFilter 用于加载提供了访问令牌的请求的授权

OAuth 2.0的提供端通过 Spring OAuth 专门的 @Configuration 适配器来简化配置。
也可以通过XML命名空间来配置OAuth，
XML的schema地址：http://www.springframework.org/schema/security/spring-security-oauth2.xsd。
命名空间：http://www.springframework.org/schema/security/oauth2

配置授权服务(Authorization Server)
----------------------------

配置授权服务时，必须要考虑使用哪种许可类型（grant type），grant type 用于客户端从终端用户获取 access token，
（常用的 grant type 比如：authorization code 授权码, user credentials 用户凭证, refresh token刷新令牌）。
服务配置用来提供一些实现：客户详情服务、令牌服务和是否启用某些方面的全局机制。
注意，各个客户端可以单独配置权限，从而使用特定的授权机制和访问许可。
也就是说，仅仅给提供端配置了支持“client credentials”许可类型，并不意味着某个特定客户端就被授权使用这种许可类型。

@EnableAuthorizationServer 加上一个实现了 AuthorizationServerConfigurer 的 @Bean 一起用于配置 OAuth 2.0 授权服务机制。

AuthorizationServerConfigurer 包含以下配置：
- ClientDetailsServiceConfigurer: 定义 client details service。客户详情可以预置也可以引用已有的存储。
- AuthorizationServerSecurityConfigurer: 定义 token endpoints 的安全限制。
- AuthorizationServerEndpointsConfigurer: 定义 authorization endpoints 和 token endpoints 以及 token services。

在授权码许可（authorization code）中，授权码被提供给客户端的方式很重要。
授权码是这样被获取的，直接把终端用户导向一个授权页面，用户输入凭证后，获得授权码，
然后拿着授权码从授权服务器重定向到客户端。

配置客户详情（Client Details）
----------------------

ClientDetailsServiceConfigurer (AuthorizationServerConfigurer 的回调) 用于定义客户详情服务的内存或jdbc实现。
客户端重要的属性有：

- clientId: (必须) 客户ID。
- secret: (可信客户端必须) 客户密码。
- scope: 客户权限。如果权限未定义或定义为空（默认为空），客户端不受权限控制。
- authorizedGrantTypes: 客户端被授权使用的许可类型。默认值为空。
- authorities: 客户端授权(通常是 Spring Security authorities)。

客户详情（Client details）可以通过应用直接修改底层存储来更新
（例如，JdbcClientDetailsService 可以直接修改数据库表），
或者通过 ClientDetailsManager 接口来更新
（都是 ClientDetailsService 的实现）

> 注意： JDBC 服务的 schema 并没有打包到库中
> (因为在实践中有太多变数)，
> 但是 github 上提供了 [测试示例](https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/test/resources/schema.sql)

管理令牌（Token）
-----------

AuthorizationServerTokenServices 接口里定义了 OAuth 2.0 令牌的操作方法。 注意以下几点：

- 创建 access token 时，必须保存 authentication ，这样接受了token的资源才可以在后面继续引用它。
- access token 用于加载创建自己的 authentication。

创建 AuthorizationServerTokenServices 接口的实现类时，你可以考虑使用 DefaultTokenServices 类，它有许多可插入的策略，用于更变 access token 的格式和存储。
在默认情况下，它使用随机值创建令牌，处理所有事情，除了 token 的持久化，持久化被委托给了 TokenStore。
token 的默认存储采用 [in-memory 实现](http://docs.spring.io/spring-security/oauth/apidocs/org/springframework/security/oauth2/provider/token/store/InMemoryTokenStore.html)，但也有一些其它的存储方式。 下面是其中一些方式的简介：

- InMemoryTokenStore（默认）适用于单服务器场景（低阻塞，宕机时无需热切换到备份服务器）。大部分项目可以在开始时或者在开发模式下使用这种方式，这样比较容易启动一个没有其它依赖的服务器。

- JdbcTokenStore 类是实现存储令牌的JDBC 版本，它将令牌信息保存到关系型数据库。 如果服务器间共享数据库或者同一个服务器有多个实例或者授权服务器、资源服务器有多个组件，那么可以使用JDBC方式存储令牌。 使用JdbcTokenStore 类时，需要将spring-jdbc组件jar包添加到工程的 classpath 中。

- JWT (JSON Web Token)版本将关于 grant 的所有数据编码到了token当中（因此不需要在后台存储数据，这就是JWT一个重要的优点）。 缺点是你不能方便地撤销一个已授权的令牌（因此一般它们被授权的有效期较短，撤销授权的操作在刷新令牌中进行）。 另一个缺点是存储的令牌数据会越来越大因为在令牌里面存储了大量的用户证书信息。 JwtTokenStore 类不是一个真正的存储类，它未持久化（保存）任何数据，但是在传输令牌信息和授权信息方面，它在 DefaultTokenServices 类中扮演了同样的角色。

> 注意：为了防止客户端应用在创建 token 时争用同一行，一定要添加 @EnableTransactionManagement 注解。
> 注意示例中的 schema 有明确的主键声明 - 这些在并发环境中同样必要。

JWT Tokens
----------

要使用 JWT token 你需要在授权服务中使用 JwtTokenStore。
JwtTokenStore（接口）类依赖JwtAccessTokenConverter类，授权服务器和资源服务器都需要接口的实现类（因此他们可以安全地使用相同的数据并进行解码）。 令牌以默认方式进行签名，资源服务器为了能够验证这些签名，它需要有与授权服务器相同的对称密钥（服务器共享对称密钥），或者它需要有可以匹配签名私钥的公钥（公有私有密钥或混合密钥）。 为了使用JwtTokenStore ，你需要在工程编译路径下添加spring-security-jwt组件jar包（你可以在Spring OAuth的github资源库中找到，但是两者的版本号是不一致的）。

许可类型（Grant Types）
-----------------

通过 AuthorizationServerEndpointsConfigurer 可以配置 AuthorizationEndpoint  支持的许可类型。
默认情况下，除了 password 许可类型外，它支持所有的类型(如何开启/关闭详情如下)。以下属性影响许可类型：

- authenticationManager：通过注入 authenticationManager 开启 password 许可。
- userDetailsService：如果注入了 UserDetailsService 或者被配置为全局（比如在 GlobalAuthenticationManagerConfigurer 中配置），
   那么 refresh token 许可将检查 user details，保证账户依然是激活的。
- authorizationCodeServices：为 auth code 许可定义授权码服务。
- implicitGrantService：在 imlpicit 许可中管理状态。
- tokenGranter: 使用 Token 许可(完全掌控许可，忽略上述其他属性)

在 XML 中 grant type 作为 authorization-server 的子元素被包括。

配置端点的URL（Endpoint URLs）
----------

AuthorizationServerEndpointsConfigurer有一个pathMapping()方法。
该方法有两个参数：

- defaultPath 默认的端点URL（框架实现）
- customPath 自定义的URL（以“/”开始）

框架自己提供的URL路径是
/oauth/authorize（授权端 the authorization endpoint），
/oauth/token (令牌端 the token endpoint)，
/oauth/confirm_access (用户发送确认授权到这里)，还有
/oauth/error (用户呈现授权服务器授权出错的请求)。
/oauth/check_token (用于资源服务器解码 access token), and
/oauth/token_key (如果使用JWT token，用于暴露token验证用的 public key).

注意：授权端/oauth/authorize（或者是它的映射）应该是受 Spring Security 保护的，所以它只能被已授权的用户访问。
比如使用标准 Spring Security 的 WebSecurityConfigurer：

```
   @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests().antMatchers("/login").permitAll().and()
        // default protection for all resources (including /oauth/authorize)
            .authorizeRequests()
                .anyRequest().hasRole("USER")
        // ... more configuration, e.g. for form login
    }
```

> 注意：如果授权服务同时也是资源服务，那么会有另一个security filter chain，使用更低优先级控制 API 资源。
> 对于那些被 access token 保护的请求，不要让他们的路径匹配面向用户的 filter chain，所以要保证包含一个请求匹配器，用来在上述 WebSecurityConfigurer 中挑出非 API 资源。

令牌端默认是通过使用支持使用HTTP基本身份验证客户机的秘密的注解@Configuration，被Spring Oauth保护的，但不是使用XML文件的（所以在这种情况下它被保护是很明确的）。

使用XML的<authorization-server/>元素可以使用一些属性来达到改变默认的端点URL。

自定义 UI
------

大多数授权服务端点主要用于机器，
但是这些端点需要界面——/oauth/confirm_access 的GET请求，/oauth/error 返回的 HTML 页面。
在框架中它们通过 whitelabel 提供，
实践中大多数授权服务想要有自己的界面，以控制样式和内容。

因为框架的默认行为在 dispatcher 中有更低的优先级，
所以只需为这些端点提供标记了 @RequestMappings 的 Spring MVC controller 就可以了。
在/oauth/confirm_access 端点，可以预期绑定到 session 的 AuthorizationRequest 会携带用户许可需要的所有数据。
（默认的实现是 WhitelabelApprovalEndpoint，可以直接复制它来开始）

你可以从这个请求获取所有的数据，然后用任意方式渲染它，
然后用户只需带着许可是否被批准的信息 POST 回到 /oauth/authorize。
请求参数直接被传到授权端点的一个 UserApprovalHandler 中，
所以你可以根据自己的喜好解释一下数据。
默认用哪个 UserApprovalHandler 取决于你是否在 AuthorizationServerEndpointsConfigurer 里提供了 ApprovalStore，
如果配了的话就是 ApprovalStoreUserApprovalHandler，
没配的话就是 TokenStoreUserApprovalHandler。
标准的 approval handlers 接受以下配置：

- TokenStoreUserApprovalHandler: 通过设置 user_oauth_approval = "true" / "false" 作出的简单的 yes/no 决策.
- ApprovalStoreUserApprovalHandler: 一系列的 scope.\* 参数，\* 表示请求的范围。
  参数值为"true" 或 "approved"表示用户批准了许可，否则认为用户拒绝了该 scope。
  如果至少有一个scope被批准，那么许可成功。

> 注意：不要忘记在你渲染的 form 中配置 CSRF 保护，
  Spring Security 默认期望收到一个请求参数"_csrf"，
  配置它将在请求属性中提供这个值。
  你可以在 Spring Security user guide 查看更多信息，
  或者查看 whitelabel 实现作为指导。

Enforcing SSL
-------------

Plain HTTP is fine for testing but an Authorization Server should only be used over SSL in production. You can run the app in a secure container or behind a proxy and it should work fine if you set the proxy and the container up correctly (which is nothing to do with OAuth2). You might also want to secure the endpoints using Spring Security requiresChannel() constraints. For the /authorize endpoint is up to you to do that as part of your normal application security. For the /token endpoint there is a flag in the AuthorizationServerEndpointsConfigurer that you can set using the sslOnly() method. In both cases the secure channel setting is optional but will cause Spring Security to redirect to what it thinks is a secure channel if it detects a request on an insecure channel.

自定义错误处理 Error Handling
----------------------

授权服务的错误处理使用标准 Spring MVC 特征，
即 endpoint 中的 @ExceptionHandler 方法。
根据其渲染方式，用户也能自己提供 WebResponseExceptionTranslator 给 endpoint，
这是更改应答内容的最好方式。
在token endpoint和 authorization endpoint 的授权错误页(/oauth/error)，
异常的渲染委托给了 HttpMesssageConverters （可以被添加到 MVC 配置中）。
whitelabel error endpoint 被提供给 HTML 应答，
但是用户可能需要提供一个自定义的实现（比如：添加一个@Controller 实现 @RequestMapping("/oauth/error")）。

映射用户角色到作用域（Mapping User Roles to Scopes）
----------------------------------------

在限制 token 的作用域时，有时这样做很有用，
不仅仅根据指派给客户端的 scope 来限制作用域，也根据用户自己的权限来限制。
如果在 AuthorizationEndpoint 使用 DefaultOAuth2RequestFactory，
你可以设置一个标识 checkUserScopes=true 来限制允许 scope，只给那些匹配用户role的。
你也可以注入一个 OAuth2RequestFactory 到 TokenEndpoint，
但是那只会在你也配置了一个 TokenEndpointAuthenticationFilter  的情况下生效（如，使用 password 许可）-
你只需要在 HTTP 的 BasicAuthenticationFilter 的后面添加 filter。
当然，你也可以实现你自己映射 scope 到 role 的规则，
实现你自己版本的 OAuth2RequestFactory。
AuthorizationServerEndpointsConfigurer  允许你注入一个自定义的 OAuth2RequestFactory ，
所以如果你使用了 @EnableAuthorizationServer，你可以使用这个特性来实现一个工厂。

资源服务器配置
------------------------------------

资源服务器可以和授权服务器在一个服务器或分开。服务器上的资源由 OAuth2 的 token 保护。
Spring OAuth 提供一种 Spring Security 认证 filter 实现保护。
通过注解 @EnableResourceServer 和 @Configuration 来开启功能，通过 ResourceServerConfigurer 来配置功能。以下特性可配置：

- tokenServices: 定义 token services 的 bean (ResourceServerTokenServices 的实例).
- resourceId: 资源 id (可选，但推荐，如果提供则将被授权服务验证).
- 资源服务的其他扩展点(比如：用于从请求中提取 token 的 tokenExtractor)
- 受保护资源请求匹配(默认全部)
- 受保护资源请求规则(默认普通授权 plain "authenticated")
- Spring Security 中 HttpSecurity 配置的用于受保护资源的其他自定义

@EnableResourceServer 注解自动给 Spring Security filter chain 添加了一个 OAuth2AuthenticationProcessingFilter。
在 XML 中有个 <resource-server/> 标签，含 id 属性，这是 bean 的 id，用于 servlet Filter，可以被手动添加到标准 Spring Security chain。

ResourceServerTokenServices 是联系授权服务的另一部分。
如果资源服务和授权服务在同一个应用，而且使用的是 DefaultTokenServices，则不必想太多，
因为它自动实现了所有必须的接口。
如果资源服务在分离的应用中，则必须确保匹配授权服务的要求，提供知道怎么正确解码 token 的 ResourceServerTokenServices。
跟授权服务一样，你可以使用 DefaultTokenServices，这个选项经常被 tokenstore 用来表达（后端存储或本地编码）。
一个可选项是 RemoteTokenServices，这是 Spring OAuth 的特性，允许资源服务通过授权服务的 http 资源(/oauth/check_token)解码 token。
用 RemoteTokenService 很方便（每个请求必须被授权服务验证），资源服务如果没有大流量或者可以承担得起 cache 这些结果的话。
为了使用 /oauth/check_token 端点，你需要通过更改 AuthorizationServerSecurityConfigurer 中的访问规则来暴露它(默认是 "denyAll()")。
如：

```
@Override
public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
    oauthServer.tokenKeyAccess("isAnonymous() || hasAuthority('ROLE_TRUSTED_CLIENT')").checkTokenAccess(
            "hasAuthority('ROLE_TRUSTED_CLIENT')");
}
```

在这个示例中我们配置了 /oauth/check_token 端点和 /oauth/token_key 端点（所以被信任的资源可以获取 public key 用于 JWT 认证）。
这两个端点使用 client credentials 许可被 HTTP 基础认证保护。


#### 配置授权感知表达式处理器 OAuth-Aware Expression Handler

你也许想利用 Spring Security 的基于表达式的 access control。
expression handler 在 @EnableResourceServer 配置中被注册。
表达式包括：#oauth2.clientHasRole, #oauth2.clientHasAnyRole, 以及 #oath2.denyClient，
可以被用于提供基于OAuth客户端的角色的访问（详见 OAuth2SecurityExpressionMethods）。
在 XML 中你可以注册 oauth-aware expression handler，通过常规的<http/> security 配置的 expression-handler 标签。


OAuth 2.0 Client
----------------

OAuth 2.0 客户端机制与访问 OAuth 2.0 受其他服务器保护资源相对应。
配置包括发布相关受保护资源给哪个访问用户。
客户端可能也需要提供保存授权码的机制，和为用户获取token的机制。

#### 受保护资源配置 Protected Resource Configuration

受保护资源（或“远程资源”）可以使用 OAuth2ProtectedResourceDetails 类型的 bean 来定义。
受保护资源有以下属性：

- id: 资源id，仅用于客户端寻找资源; 不用于 OAuth 协议。同时也是 bean 的id。
- clientId: OAuth 客户端 id。OAuth 提供端用于标识 客户端的id。
- clientSecret: 资源相关密码。默认密码不为空。
- accessTokenUri: 提供者授权端点 URI，提供 access token。
- scope: 用逗号分隔的字符串列表，说明指定访问资源的 scope。默认没有 scope 被指定。
- clientAuthenticationScheme: 用于认证 access token endpoint 的客户端 scheme。建议值："http_basic" 和 "form"。默认: "http_basic"。
  查看 OAuth 2 的 2.1 小结。

不同的 grant types 有不同的 OAuth2ProtectedResourceDetails 具体实现（如："client_credentials" grant type 的 ClientCredentialsResource）。
对于需要用户认证的 grant type ，有一个更进一步的属性：

    userAuthorizationUri: 如果需要授权访问资源，用户将被重定向到那个uri。注意，不是必须的，取决于哪个 OAuth2 profiles 被支持。

在 XML 中有个 <resource/> 标签可以用于创建 OAuth2ProtectedResourceDetails 类型的 bean。
他有所有上述属性。


Client Configuration
--------------------

For the OAuth 2.0 client, configuration is simplified using @EnableOAuth2Client. This does 2 things:

- Creates a filter bean (with ID oauth2ClientContextFilter) to store the current request and context. In the case of needing to authenticate during a request it manages the redirection to and from the OAuth authentication uri.

- Creates a bean of type AccessTokenRequest in request scope. This can be used by authorization code (or implicit) grant clients to keep state related to individual users from colliding.

The filter has to be wired into the application (e.g. using a Servlet initializer or web.xml configuration for a DelegatingFilterProxy with the same name).

The AccessTokenRequest can be used in an OAuth2RestTemplate like this:

```
@Autowired
private OAuth2ClientContext oauth2Context;

@Bean
public OAuth2RestTemplate sparklrRestTemplate() {
	return new OAuth2RestTemplate(sparklr(), oauth2Context);
}
```

The OAuth2ClientContext is placed (for you) in session scope to keep the state for different users separate. Without that you would have to manage the equivalent data structure yourself on the server, mapping incoming requests to users, and associating each user with a separate instance of the OAuth2ClientContext.

In XML there is a <client/> element with an id attribute - this is the bean id for a servlet Filter that must be mapped as in the @Configuration case to a DelegatingFilterProxy (with the same name).

Accessing Protected Resources
-----------------------------

Once you've supplied all the configuration for the resources, you can now access those resources. The suggested method for accessing those resources is by using the RestTemplate introduced in Spring 3. OAuth for Spring Security has provided an extension of RestTemplate that only needs to be supplied an instance of OAuth2ProtectedResourceDetails. To use it with user-tokens (authorization code grants) you should consider using the @EnableOAuth2Client configuration (or the XML equivalent <oauth:rest-template/>) which creates some request and session scoped context objects so that requests for different users do not collide at runtime.

As a general rule, a web application should not use password grants, so avoid using ResourceOwnerPasswordResourceDetails if you can in favour of AuthorizationCodeResourceDetails. If you desparately need password grants to work from a Java client, then use the same mechanism to configure your OAuth2RestTemplate and add the credentials to the AccessTokenRequest (which is a Map and is ephemeral) not the ResourceOwnerPasswordResourceDetails (which is shared between all access tokens).

Persisting Tokens in a Client
-----------------------------

A client does not need to persist tokens, but it can be nice for users to not be required to approve a new token grant every time the client app is restarted. The ClientTokenServices interface defines the operations that are necessary to persist OAuth 2.0 tokens for specific users. There is a JDBC implementation provided, but you can if you prefer implement your own service for storing the access tokens and associated authentication instances in a persistent database. If you want to use this feature you need provide a specially configured TokenProvider to the OAuth2RestTemplate e.g.

```
@Bean
@Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
public OAuth2RestOperations restTemplate() {
	OAuth2RestTemplate template = new OAuth2RestTemplate(resource(), new DefaultOAuth2ClientContext(accessTokenRequest));
	AccessTokenProviderChain provider = new AccessTokenProviderChain(Arrays.asList(new AuthorizationCodeAccessTokenProvider()));
	provider.setClientTokenServices(clientTokenServices());
	return template;
}
```

Customizations for Clients of External OAuth2 Providers
-------------------------------------------------------

Some external OAuth2 providers (e.g. Facebook) do not quite implement the specification correctly, or else they are just stuck on an older version of the spec than Spring Security OAuth. To use those providers in your client application you might need to adapt various parts of the client-side infrastructure.

To use Facebook as an example, there is a Facebook feature in the tonr2 application (you need to change the configuration to add your own, valid, client id and secret - they are easy to generate on the Facebook website).

Facebook token responses also contain a non-compliant JSON entry for the expiry time of the token (they use expires instead of expires_in), so if you want to use the expiry time in your application you will have to decode it manually using a custom OAuth2SerializationService.

https://github.com/spring-projects/spring-security-oauth/blob/master/docs/oauth2.md